package ToyProject1.hungrytech.controller;

import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.memberDto.MemberForm;
import ToyProject1.hungrytech.memberDto.MemberInfo;
import ToyProject1.hungrytech.memberDto.MemberLoginForm;
import ToyProject1.hungrytech.memberDto.MemberLoginInfo;
import ToyProject1.hungrytech.service.board.BoardService;
import ToyProject1.hungrytech.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final HttpSession session;

    /**
     * 로그인
     * 로그아웃
     */
    @GetMapping("/loginForm")
    public String login(Model model) {
        model.addAttribute("memberLoginForm", new MemberLoginForm());
        return "login/loginPage";
    }

    @PostMapping("/member/login")
    public String login_pro(MemberLoginForm loginForm, Model model) {
        Member result = memberService.login(loginForm);

        //로그인 성공
        if(result!=null) {

            session.setAttribute("memberInfo",
                    new MemberLoginInfo(result.getAccountId(), result.getName()));

            return "login/login_success";
        }

        String loginfailMessage = "아이디 혹은 암호가 올바르지 않습니다.";
        model.addAttribute("failMessage", loginfailMessage);

        //id 저장 처리
        if(loginForm.isSaveId()) {
            loginForm.setAccountPw("");
            return "login/loginPage";
        }

        //id 저장체크를 하지 않았을 때
        loginForm.setAccountId("");
        loginForm.setAccountPw("");
        return "login/loginPage";


    }

    @GetMapping("/logoutPage")
    public String logout() {
        session.invalidate();
        return "logout/logout_success";
    }

    /**
     * 회원가입
     * 회원탈퇴
     */
    @GetMapping("/member/new")
    public String join(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "join/joinPage";
    }

    @PostMapping("/member/new")
    public String join_pro(MemberForm memberForm) {
        memberService.join(memberForm);
        return "join/join_success";
    }

    //회원탈퇴
    @GetMapping("/member/{id}/delete")
    public String memberDelete(@PathVariable("id") String accountId) {
        session.invalidate();
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
            model.addAttribute("boards", boardPage.getContent());
            return "mypage/mypage";
        }

        Member findMember = boardPage
                .getContent()
                .get(0)
                .getMember();

        model.addAttribute("memberInfo", createMemberInfo(findMember));
        model.addAttribute("boards", boardPage.getContent());
        model.addAttribute("boardPage", boardPage);
        return "mypage/mypage";
    }

    private MemberInfo createMemberInfo(Member findMember) {
        return new MemberInfo(
                findMember.getName(),
                findMember.getAccountId(),
                findMember.getEmail(),
                findMember.getPhoneNumber()
        );
    }

    @PostMapping("/member/{id}/edit")
    public String mypageInfoChange(MemberInfo memberInfo, Model model) {

        memberService.changeInfo(memberInfo);

        return "memberInfoChange/infochange_success";
    }



}
