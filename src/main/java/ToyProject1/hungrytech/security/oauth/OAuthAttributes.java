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
        if(registrationId.equals("naver")) {
            return ofNaver("id", attributes);
        }
        return ofKakao("id", attributes);
    }



    public Member toEntity(String phoneNumber) {

        MemberForm memberForm = new MemberForm();
        memberForm.setName(name);
        memberForm.setAccountId(extractAccountId(email));
        memberForm.setAccountPw(name + "oauth");
        memberForm.setEmail(email);
        memberForm.setPhoneNumber(phoneNumber);

        if(phoneNumber.equals("kakao")){
            memberForm.setOauth(Oauth.KAKAO);
        }else {
            memberForm.setOauth(Oauth.NAVER);
        }

        return Member.createMember(memberForm);
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .nickName("")
                .email((String) response.get("email"))
                .attributes(response)
                .phoneNumber((String) response.get("mobile"))
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        //이메일 동의를 하지 않을경우.
        String email = null;

        if(kakaoAccount.get("email") == null) {
            email = "이메일 등록을 해주세요.";
        }else {
            email = (String) kakaoAccount.get("email");
        }

        //카카오는 사업자 등록증이 없으면 핸드폰번호를 반환하지 않는다.
        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .nickName((String) profile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .phoneNumber("kakao")
                .build();
    }

    private String extractAccountId(String email) {
        int index = email.indexOf("@");
        return email.substring(0, index);
    }
}
