package converter;

import ToyProject1.hungrytech.domain.member.Member;
import ToyProject1.hungrytech.memberDto.MemberInfo;

public class MemberConverter {

    public MemberInfo convertMemberToMemberInfo(Member member) {
        return MemberInfo.builder()
                .accountId(member.getAccountId())
                .name(member.getName())
                .email(member.getEmail())
                .oauth(member.getOauth())
                .phoneNumber(member.getPhoneNumber())
                .build();

    }


}
