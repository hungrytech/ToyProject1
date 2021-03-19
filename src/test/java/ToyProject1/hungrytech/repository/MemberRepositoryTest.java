package ToyProject1.hungrytech.repository;

import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.memberDto.MemberForm;
import ToyProject1.hungrytech.memberDto.MemberLoginForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void before() {
        MemberForm memberForm = new MemberForm();
        memberForm.setName("유저1");
        memberForm.setAccountId("user1");
        memberForm.setAccountPw("1234");
        memberForm.setEmail("user1@gmail.com");
        memberForm.setPhoneNumber("010-1110-1111");

        //패스워드 암호화
        memberForm.setAccountPw(passwordEncoder.encode(memberForm.getAccountPw()));

        Member member = Member.createMember(memberForm);

        memberRepository.save(member);
    }

    @Test
    @DisplayName("회원 폰번호 변경 테스트")
    public void MemberPhoneNumberChange() {
        //given

        List<Member> members = memberRepository.findAll();

        Member member = members.get(0);

        //when

        member.changePhoneNumber("010-5555-5544");

        Long id = member.getId();

        Optional<Member> findMember = memberRepository.findById(id);
        Member result = null;
        if(findMember.isPresent()) {
            result = findMember.get();
        }

        //then
        assertThat(result.getPhoneNumber()).isEqualTo("010-5555-5544");

    }

    @Test
    @DisplayName("회원 pw 변경 테스트")
    public void MemberPwChange() {
        //given
        List<Member> members = memberRepository.findAll();

        Member member = members.get(0);

        //when

        member.changePw(passwordEncoder.encode("ggggg"));
        String accountPw = member.getAccountPw();

        //then
        assertThat(passwordEncoder.matches("ggggg", accountPw)).isTrue();
    }

    @Test
    @DisplayName("회원 email 변경 테스트")
    public void MemberEmailChange() {
        //given
        List<Member> members = memberRepository.findAll();

        Member member = members.get(0);

        //when

        member.changeEmail("change@naver.com");

        Long id = member.getId();

        Optional<Member> findMember = memberRepository.findById(id);
        Member result = null;
        if(findMember.isPresent()) {
            result = findMember.get();
        }

        //then
        assertThat(result.getEmail()).isEqualTo("change@naver.com");

    }

    @Test
    @DisplayName("accountId로 회원 조회 테스트")
    public void findByAccountId() {
        List<Member> members = memberRepository.findAll();

        Member member = members.get(0);

        Member findMember = memberRepository.findMemberByAccountId("user1");

        assertThat(findMember).isSameAs(member);
    }

    @Test
    @DisplayName("로그인관련 Id Check 조회 테스트")
    public void loginTest() {
        //given
        MemberLoginForm memberLoginForm = new MemberLoginForm();
        memberLoginForm.setAccountId("user1");
        memberLoginForm.setAccountPw("1234");

        //when
        Optional<Member> result = memberRepository
                .findLoginCheckByAccountId(memberLoginForm.getAccountId());

        //then

        assertThat(result.isPresent()).isTrue();
    }

}
