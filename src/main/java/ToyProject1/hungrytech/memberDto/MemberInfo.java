package ToyProject1.hungrytech.memberDto;

import ToyProject1.hungrytech.domain.member.Oauth;
import lombok.Builder;
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

    private Oauth oauth;

    @Builder
    public MemberInfo(String name, String accountId, String email, String phoneNumber, Oauth oauth) {
        this.name = name;
        this.accountId = accountId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.oauth = oauth;
    }


}
