package ToyProject1.hungrytech.service.member;


import ToyProject1.hungrytech.domain.member.Member;
import ToyProject1.hungrytech.memberDto.MemberForm;
import ToyProject1.hungrytech.memberDto.MemberInfo;
import ToyProject1.hungrytech.memberDto.MemberLoginForm;
import ToyProject1.hungrytech.repository.BoardRepository;
import ToyProject1.hungrytech.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void before() {
        MemberForm memberForm = new MemberForm();
        memberForm.setName("유저1");
        memberForm.setAccountId("user1");
        memberForm.setAccountPw("1234");
        memberForm.setEmail("user1@gmail.com");
        memberForm.setPhoneNumber("010-1110-1111");

        //패스워드 암호화
        memberForm.setAccountPw(passwordEncoder.encode(memberForm.getAccountPw()));


        memberService.join(memberForm);
    }

    @Test
    @DisplayName("회원가입 테스트")
    void joinTest() {
        //given
        MemberForm memberForm = new MemberForm();
        memberForm.setName("유저1");
        memberForm.setAccountId("user2");
        memberForm.setAccountPw("1234");
        memberForm.setEmail("user1@gmail.com");
        memberForm.setPhoneNumber("010-1110-1111");

        //패스워드 암호화
        memberForm.setAccountPw(passwordEncoder.encode(memberForm.getAccountPw()));


        memberService.join(memberForm);

        //when
        Member findMember = memberService.findInfo(memberForm.getAccountId());

        //then
        assertThat(findMember).isNotNull();


    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void withDrawal() {
        //given
        Member findMember = memberRepository.findMemberByAccountId("user1");

        //when
        memberService.withDrawal(findMember.getAccountId());

        List<Member> findMembers = memberRepository.findAll();

        //then
        assertThat(findMembers.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("회원 정보 조회 테스트")
    void memberInfo() {
        //given
        Member member = memberRepository.findMemberByAccountId("user1");

        //when
        Member findMember = memberService.findInfo(member.getAccountId());

        //then
        assertThat(findMember).isSameAs(member);
    }

    @Test
    @DisplayName("회원 정보 변경 테스트")
    void changeInfo() {
        //given
        Member member = memberRepository.findMemberByAccountId("user1");

        //MemberInfo
        MemberInfo memberInfo = new MemberInfo();

        //변경불가 정보
        memberInfo.setAccountId(member.getAccountId());
        memberInfo.setName(member.getName());

        //변경퇼 정보
        memberInfo.setEmail("userchange@gmail.com");
        memberInfo.setAccountPw("abcd");
        memberInfo.setPhoneNumber("010-5431-3215");

        //when
        memberService.changeInfo(memberInfo);

        Member findMember = memberService.findInfo(member.getAccountId());

        //then
        assertThat(findMember.getEmail()).isEqualTo(memberInfo.getEmail());

        assertThat(passwordEncoder.matches("abcd", findMember.getAccountPw())).isTrue();

        assertThat(findMember.getPhoneNumber()).isEqualTo(memberInfo.getPhoneNumber());
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() {
        //given
        MemberLoginForm memberLoginForm1 = new MemberLoginForm();
        memberLoginForm1.setAccountId("user1");
        memberLoginForm1.setAccountPw("1234");

        MemberLoginForm memberLoginForm2 = new MemberLoginForm();
        memberLoginForm2.setAccountId("user2");
        memberLoginForm2.setAccountPw("abc1234");

        //when
        Member member1 = memberService.login(memberLoginForm1);

        Member member2 = memberService.login(memberLoginForm2);


        //then
        assertThat(member1).isNotNull();

        assertThat(member2).isNull();


    }

    @Test
    @DisplayName("로그인 증복Id check 테스트")
    void loginExistIdTest() {

        Optional<Member> optionalMember = memberService.checkId("user1");

        assertThat(optionalMember).isNotNull();


    }

    @Test
    @DisplayName("아이디 찾기 테스트")
    void findAccountIdTest() {
        //given
        String userName = "유저1";
        String userEmail = "user1@gmail.com";

        //when
        String result = memberService.findMemberAccountId(userName, userEmail);

        //then
        assertThat(result).isNotNull();

        assertThat(result).isEqualTo("user1");


    }

    @Test
    @DisplayName("비밀번호 찾기 테스트")
    void findAccountPw() {
        //given
        String userAccountId = "user1";
        String userName = "유저1";
        String userEmail = "user1@gmail.com";

        //when
        Member member = memberService
                .findMemberAccountPw(userAccountId, userName, userEmail);

        //then
        assertThat(member).isNotNull();
        assertThat(member)
                .extracting("accountId", "name", "email")
                .contains("user1", "유저1", "user1@gmail.com");
    }


}