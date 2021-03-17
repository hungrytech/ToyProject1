package ToyProject1.hungrytech.repository;

import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.member.Member;
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
        Member member = Member.createMember("UserA",
                passwordEncoder.encode("sfdfd"),
                "user@naver.com",
                "010-1521-1622");
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
        Member member = Member.createMember("UserB",
                passwordEncoder.encode("sfdfd"),
                "userB@naver.com",
                "010-1521-1622");

        memberRepository.save(member);
        //when
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        Member findMember = null;
        if (optionalMember.isPresent()) {
            findMember = optionalMember.get();
        }
        findMember.changePw(passwordEncoder.encode("ggggg"));
        String accountPw = findMember.getAccountPw();

        //then
        assertThat(passwordEncoder.matches("ggggg", accountPw)).isTrue();
    }

    @Test
    @DisplayName("회원 email 변경 테스트")
    public void MemberEmailChange() {
        //given
        Member member = Member.createMember("UserC",
                passwordEncoder.encode("sfdfd"),
                "userC@naver.com",
                "010-1521-1622");

        memberRepository.save(member);
        //when
        Optional<Member> optionalMember = memberRepository.findById(member.getId());
        Member findMember = null;
        if (optionalMember.isPresent()) {
            findMember = optionalMember.get();
        }
        findMember.changeEmail("change@naver.com");

        //then
        assertThat(findMember.getEmail()).isEqualTo("change@naver.com");

    }

    @Test
    @DisplayName("accountId로 회원 조회")
    public void findByAccountId() {
        Member member = Member.createMember("userId",
                passwordEncoder.encode("sdfd"),
                "userB@naver.com",
                "010-2422-4444");

        memberRepository.save(member);

        Member findMember = memberRepository.findMemberByAccountId("userId");

        assertThat(findMember).isSameAs(member);
    }

    @Test
    @DisplayName("해당 회원이쓴 게시글들을 모두 조회")
    public void findMemberAndBoard() {
        //given
        Member member = Member.createMember("userD",
                passwordEncoder.encode("abcd1"),
                "user@gmail.com",
                "010-1234-1234");

        Board board1 = Board.createBoard("제목1",
                "게시글1",
                "C:",
                member);
        Board board2 = Board.createBoard("제목2",
                "게시글2",
                "C:",
                member);
        memberRepository.save(member);
        boardRepository.save(board1);
        boardRepository.save(board2);

        //when
        Member findMember = memberRepository.findMemberAndBoard("userD");
        List<Board> boards = member.getBoards();

        //then
        assertThat(boards.size()).isEqualTo(2);


    }


}
