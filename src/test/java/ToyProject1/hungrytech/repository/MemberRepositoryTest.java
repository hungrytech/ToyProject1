package ToyProject1.hungrytech.repository;

import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.memberDto.MemberForm;
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
@Rollback(value = false)
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 폰번호 변경 테스트")
    public void MemberPhoneNumberChange() {
        //given
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

        //when
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        Member findMember = null;
        if (optionalMember.isPresent()) {
            findMember = optionalMember.get();
        }
        findMember.changePhoneNumber("010-5555-5544");

        //then
        assertThat(member.getPhoneNumber()).isEqualTo("010-5555-5544");

    }

    @Test
    @DisplayName("회원 pw 변경 테스트")
    public void MemberPwChange() {
        //given
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
        //when
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        Member findMember = null;
        if (optionalMember.isPresent()) {
            findMember = optionalMember.get();
        }
        findMember.changePw(passwordEncoder.encode("ggggg"));
        String accountPw = member.getAccountPw();

        //then
        assertThat(passwordEncoder.matches("ggggg", accountPw)).isTrue();
    }

    @Test
    @DisplayName("회원 email 변경 테스트")
    public void MemberEmailChange() {
        //given
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
        //when
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        Member findMember = null;
        if (optionalMember.isPresent()) {
            findMember = optionalMember.get();
        }
        findMember.changeEmail("change@naver.com");

        //then
        assertThat(member.getEmail()).isEqualTo("change@naver.com");

    }

    @Test
    @DisplayName("accountId로 회원 조회")
    public void findByAccountId() {
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

        Member findMember = memberRepository.findMemberByAccountId("userId");

        assertThat(findMember).isSameAs(member);
    }



}
