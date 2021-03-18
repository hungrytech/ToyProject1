package ToyProject1.hungrytech.domain;

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

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
@Transactional
public class DomainCreateTest {
    @Autowired
    private EntityManager em;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 Entity 생성, 게시물 작성 테스트")
    public void createMemberTest() {
        //given
        MemberForm memberForm = new MemberForm();
        memberForm.setName("유저1");
        memberForm.setAccountId("user2");
        memberForm.setAccountPw("1234");
        memberForm.setEmail("user1@gmail.com");
        memberForm.setPhoneNumber("010-1110-1111");

        //패스워드 암호화
        memberForm.setAccountPw(passwordEncoder.encode(memberForm.getAccountPw()));

        Member member = Member.createMember(memberForm);

        Board board = Board.createBoard("첫 게시글 제목",
                "안녕하세요 첫 게시글 입니다.",
                null, member);
        em.persist(member);
        em.persist(board);
        //when
        em.flush();
        em.clear();
        Member findMember = em.find(Member.class, member.getId());
        //then
        assertThat(findMember.getBoards().get(0).getTitle()).isEqualTo("첫 게시글 제목");
    }

    @Test
    @DisplayName("passwordEncoder 구현로직 테스트")
    public void passwordEncoderTest() {
        MemberForm memberForm = new MemberForm();
        memberForm.setName("유저1");
        memberForm.setAccountId("user1");
        memberForm.setAccountPw("1234");
        memberForm.setEmail("user1@gmail.com");
        memberForm.setPhoneNumber("010-1110-1111");
        memberForm.setAccountPw(passwordEncoder.encode(memberForm.getAccountPw()));
        Member member = Member.createMember(memberForm);

        Board board = Board.createBoard("첫 게시글 제목",
                "안녕하세요 첫 게시글 입니다.",
                null, member);
        em.persist(member);
        em.persist(board);
        //when

        Member findMember = em.find(Member.class, member.getId());


        //then
        assertThat(passwordEncoder.matches("1234",findMember.getAccountPw())).isTrue();
    }
}
