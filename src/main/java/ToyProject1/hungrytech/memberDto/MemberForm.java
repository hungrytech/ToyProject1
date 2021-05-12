package ToyProject1.hungrytech.memberDto;

import ToyProject1.hungrytech.domain.member.Oauth;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class MemberForm {

    private String name;

    private String accountId;

    private String accountPw;

    private String email;

    private String phoneNumber;

    private Oauth oauth;

    private boolean duplicationCheck;



}
