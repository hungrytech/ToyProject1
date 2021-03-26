package ToyProject1.hungrytech.memberDto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MemberLoginInfo {

    private String accountId;

    private String name;

    public MemberLoginInfo(String accountId, String name) {
        this.accountId = accountId;
        this.name = name;
    }

}
