package ToyProject1.hungrytech.service.member;

import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.memberDto.MemberForm;
import ToyProject1.hungrytech.memberDto.MemberInfo;
import ToyProject1.hungrytech.memberDto.MemberLoginForm;

import java.util.Optional;

public interface MemberService {
    /**
     * 회원 가입, 탈퇴
     * 아이디 증복 조회
     */
    //회원가입
    void join(MemberForm memberForm);

    //아이디 증복 조회
    Optional<Member> checkId(String accountId);


    //회원 탈퇴
    void withDrawal(String accountId);

    /**
     * 로그인
     */
    //로그인
    Member login(MemberLoginForm loginForm);


    /**
     * MyPage
     */
    //회원 정보 조회
    Member findInfo(String accountId);


    //회원 정보 변경
    void changeInfo(MemberInfo memberInfo);

    /**
     * 아이디찾기
     * 비밀번호 찾기
     */
    String findMemberAccountId(String name, String email);
}
