package ToyProject1.hungrytech.memberDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
public class MemberFindAccountIdForm {

    @Size(min = 2, max = 4, message = "성함은 2~4글자이어야 합니다.")
    @NotEmpty(message = "성함을 입력해주세요")
    private String name;

    @Email(message = "이메일이 올바르지 않습니다.")
    @NotEmpty(message = "이메일을 입력해주세요")
    private String email;

}
