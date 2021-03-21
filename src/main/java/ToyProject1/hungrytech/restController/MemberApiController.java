package ToyProject1.hungrytech.restController;

import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.memberDto.MemberInfo;
import ToyProject1.hungrytech.service.MemberService;
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
    public MemberInfo checkIdExist(@PathVariable("id") String accountId) {
        Optional<Member> findMember = memberService.checkId(accountId);
        if(findMember.isPresent()) {
            return new MemberInfo(findMember.get());
        }
        return null;
    }
}
