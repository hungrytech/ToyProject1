package ToyProject1.hungrytech.controller;

import ToyProject1.hungrytech.boardDto.BoardInfo;
import ToyProject1.hungrytech.domain.board.Board;
import ToyProject1.hungrytech.domain.member.Member;
import ToyProject1.hungrytech.memberDto.MemberFindAccountIdForm;
import ToyProject1.hungrytech.memberDto.MemberFindAccountPwForm;
import ToyProject1.hungrytech.memberDto.MemberForm;
import ToyProject1.hungrytech.memberDto.MemberInfo;
import ToyProject1.hungrytech.service.board.BoardService;
import ToyProject1.hungrytech.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final BoardService boardService;

    /**
     * 로그인
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model) {

        if(error != null && exception != null) {
            model.addAttribute("error", error);
            model.addAttribute("errorMessage", "아이디 혹은 암호가 올바르지 않습니다.");
        }
        return "login/login_page";
    }

    /**
     * 회원가입
     * 회원탈퇴
     */
    @GetMapping("/member/new")
    public String join(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "join/join_page";
    }

    @PostMapping("/member/new")
    public String join_pro(MemberForm memberForm) {
        memberService.join(memberForm);
        return "join/join_success";
    }

    //회원탈퇴
    @GetMapping("/member/{id}/delete")
    public String memberDelete(@PathVariable("id") String accountId,
                               HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        memberService.withDrawal(accountId);
        return "withDrawal/withDrawal_success";
    }

    /**
     * Mypage
     * 회원정보변경, 자신이 쓴 게시글 조회
     */
    //회원정보 변경, 자신이 쓴 게시글 조회
    @GetMapping("/member/{id}/edit")
    public String mypage(@PathVariable("id") String accountId,
                         Model model,
                         Pageable pageable) {

        Page<Board> boardPage = boardService.getBoardListByAccountId(accountId, pageable);

        if(boardPage.getContent().size() == 0) {

            Member findMember = memberService.findInfo(accountId);

            model.addAttribute("memberInfo", createMemberInfo(findMember));
            model.addAttribute("boards", getBoards(boardPage));
            return "mypage/mypage";
        }

        Member findMember = boardPage
                .getContent()
                .get(0)
                .getMember();

        model.addAttribute("memberInfo", createMemberInfo(findMember));
        model.addAttribute("boards", getBoards(boardPage));
        model.addAttribute("boardPage", boardPage);
        return "mypage/mypage";
    }

    //회원정보 변경
    @PostMapping("/member/{id}/edit")
    public String mypageInfoChange(MemberInfo memberInfo) {

        memberService.changeInfo(memberInfo);

        return "memberInfoChange/infochange_success";
    }

    private List<BoardInfo> getBoards(Page<Board> boardPage) {
        return boardPage
                .getContent()
                .stream()
                .map(BoardInfo::new)
                .collect(Collectors.toList());
    }

    private MemberInfo createMemberInfo(Member findMember) {
        return new MemberInfo(
                findMember.getName(),
                findMember.getAccountId(),
                findMember.getEmail(),
                findMember.getPhoneNumber());
    }

    /**
     * 아이디찾기
     * 비밀번호 찾기
     */
    //아이디 찾기
    @GetMapping("/find/accountId")
    public String findAccountId(Model model) {

        model.addAttribute("findAccountIdForm", new MemberFindAccountIdForm());
        return "findAccountId/find_account_id";
    }

    @PostMapping("/find/accountId")
    public String findAccountIdResult(@Valid @ModelAttribute("findAccountIdForm")
                                                  MemberFindAccountIdForm findAccountIdForm,
                                      BindingResult result,
                                      Model model) {
        if(result.hasErrors()) {
            return "findAccountId/find_account_id";
        }

        String findAccountId = memberService
                        .findMemberAccountId(
                                findAccountIdForm.getName(),
                                findAccountIdForm.getEmail()
                        );
        if(findAccountId != null) {
            model.addAttribute("accountId", findAccountId);
            return "findAccountId/find_account_id_result";
        }

        model.addAttribute("failMessage", "아이디를 찾을 수 없습니다.");
        return "findAccountId/find_account_id_result";
    }

    //비밀번호 찾기
    @GetMapping("/find/accountPw")
    public String findAccountPw(Model model) {
        model.addAttribute("findAccountPwForm", new MemberFindAccountPwForm());
        return "findaccountpw/find_account_pw";
    }

    @PostMapping("/find/accountPw")
    public String findAccountPwResult(@Valid @ModelAttribute("findAccountPwForm")
                                                  MemberFindAccountPwForm accountPwForm,
                                      BindingResult result,
                                      Model model) {
        if(result.hasErrors()) {
            return "findaccountpw/find_account_pw";
        }
        Member findMember = memberService.findMemberAccountPw(
                accountPwForm.getAccountId(),
                accountPwForm.getName(),
                accountPwForm.getEmail());

        //해당 회원이 없을경우
        if(findMember == null) {
            String failMessage = "해당 회원을 찾을 수 없습니다.";
            model.addAttribute("failMessage", failMessage);
            return "findaccountpw/find_account_pw_fail";
        }

        model.addAttribute("memberInfo", changeMemberInfo(findMember));

        return "findaccountpw/change_account_pw";
    }


    private MemberInfo changeMemberInfo(Member member) {

        return new MemberInfo(
                member.getName(),
                member.getAccountId(),
                member.getEmail(),
                member.getPhoneNumber());

    }

    //비밀번호변경
    @PostMapping("/find/accountPw/change")
    public String changePw(@ModelAttribute("memberInfo")
                                       MemberInfo memberInfo) {
        memberService.changeInfo(memberInfo);

        return "findaccountpw/change_account_pw_success";
    }
}
