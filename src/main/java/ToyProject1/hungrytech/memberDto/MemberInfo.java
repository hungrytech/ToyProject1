package ToyProject1.hungrytech.memberDto;

import ToyProject1.hungrytech.entity.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberInfo {


    private String name;

    private String accountId;

    private String accountPw;

    private String email;

    private String phoneNumber;

    private boolean loginCheck = false;

    public void setInfo(Member member) {
        name = member.getName();
        accountId = member.getAccountId();
        email = member.getEmail();
        phoneNumber = member.getPhoneNumber();
        loginCheck = true;

    }

    public void changeInfo(MemberInfo memberInfo) {
        email = memberInfo.getEmail();
        phoneNumber = memberInfo.getPhoneNumber();
    }
}
