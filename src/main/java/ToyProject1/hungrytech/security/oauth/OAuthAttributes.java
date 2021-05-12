package ToyProject1.hungrytech.security.oauth;

import ToyProject1.hungrytech.domain.member.Member;
import ToyProject1.hungrytech.domain.member.Oauth;
import ToyProject1.hungrytech.memberDto.MemberForm;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String nickName;
    private String email;
    private String phoneNumber;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String nickName, String email, String phoneNumber) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.phoneNumber = phoneNumber;

    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        return ofNaver("id", attributes);
    }

    public Member toEntity() {
        MemberForm memberForm = new MemberForm();
        memberForm.setName(name);
        memberForm.setAccountId(extractAccountId(email));
        memberForm.setAccountPw(name + "oauth");
        memberForm.setEmail(email);
        memberForm.setPhoneNumber(phoneNumber);
        memberForm.setOauth(Oauth.NAVER);

        return Member.createMember(memberForm);
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        String nickname = (String) response.get("nickname");
        System.out.println("nickname = " + nickname);
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .nickName((String) response.get("nickname"))
                .email((String) response.get("email"))
                .attributes(response)
                .phoneNumber((String) response.get("mobile"))
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private String extractAccountId(String email) {
        int index = email.indexOf("@");
        return email.substring(0, index);
    }
}
