package ToyProject1.hungrytech.controller;

import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.memberDto.MemberForm;
import ToyProject1.hungrytech.memberDto.MemberInfo;
import ToyProject1.hungrytech.memberDto.MemberLoginForm;
import ToyProject1.hungrytech.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
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

    @PostMapping("/login_pro")
    public String login_pro(MemberLoginForm loginForm, Model model) {
        Member result = memberService.login(loginForm);

        if(result!=null) {
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.setInfo(result);

            session.setAttribute("memberInfo", memberInfo);

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
     */
    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "join/joinPage";
    }

    @PostMapping("/join_pro")
    public String join_pro(MemberForm memberForm) {
        memberService.join(memberForm);
        return "join/join_success";
    }
}
