package ToyProject1.hungrytech.restController;

import ToyProject1.hungrytech.domain.member.Member;
import ToyProject1.hungrytech.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/member/checkIdExist/{id}")
    public String checkIdExist(@PathVariable("id") String accountId) {
        boolean result = memberService.checkIdDuplication(accountId);
        return String.valueOf(result);
    }
}
