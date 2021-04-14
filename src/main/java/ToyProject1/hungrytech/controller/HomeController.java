package ToyProject1.hungrytech.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {

        return "redirect:/main";
    }
    @RequestMapping("/main")
    public String main() {
        return "index";
    }

}
