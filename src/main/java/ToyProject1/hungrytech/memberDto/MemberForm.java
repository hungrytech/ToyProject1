package ToyProject1.hungrytech.memberDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {

    private String accountId;

    private String accountPw;

    private String email;

    private String phoneNumber;

    private boolean duplicationCheck;


}
