package ToyProject1.hungrytech.controller;

import ToyProject1.hungrytech.memberDto.MemberInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String main() {

        return "home";
    }

}
