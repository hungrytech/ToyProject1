package ToyProject1.hungrytech.domainTest;

import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
@Transactional
public class domainCreateTest {
    @Autowired
    private EntityManager em;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원 Entity 생성, 저장 테스트")
    public void createMemberTest() {
        //given

        Member member = Member.createMember("first1",
                passwordEncoder.encode("sfdsfd"),
                "first@naver.com",
                "010-1100-0111");
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
        Member member = Member.createMember("first2",
                passwordEncoder.encode("sfdsfd"),
                "first2@naver.com",
                "010-1100-0111");
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
        assertThat(passwordEncoder.matches("sfdsfd",findMember.getAccountPw())).isTrue();
    }
}
