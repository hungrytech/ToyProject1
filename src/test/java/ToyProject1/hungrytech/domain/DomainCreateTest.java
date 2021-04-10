package ToyProject1.hungrytech.domain;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.boardcommentDto.BoardCommentForm;
import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.boardcomment.BoardComment;
import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.memberDto.MemberForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class DomainCreateTest {
    @Autowired
    private EntityManager em;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void before() {

        //Member
        MemberForm memberForm = new MemberForm();
        memberForm.setName("유저1");
        memberForm.setAccountId("user1");
        memberForm.setAccountPw("1234");
        memberForm.setEmail("user1@gmail.com");
        memberForm.setPhoneNumber("010-1110-1111");

        //패스워드 암호화
        memberForm.setAccountPw(passwordEncoder.encode(memberForm.getAccountPw()));

        Member member = Member.createMember(memberForm);

        //Board
        BoardForm boardForm = new BoardForm();
        boardForm.setTitle("게시글 제목");
        boardForm.setContent("게시글 본문");
        boardForm.setImgPath("C:");

        Board board = Board.createBoard(boardForm, member);

        em.persist(member);
        em.persist(board);

        BoardCommentForm boardCommentForm = new BoardCommentForm();
        boardCommentForm.setContent("안녕하세요");

        BoardComment boardComment = BoardComment
                .createdBoardComment(boardCommentForm, member, board);

        em.persist(boardComment);
    }


    @Test
    @DisplayName("회원 Entity 생성, 게시물 작성 테스트")
    public void createMemberTest() {

        Member findMember = em.createQuery("select distinct m from Member m " +
                "join fetch m.boards where m.accountId = :accountId", Member.class)
                .setParameter("accountId", "user1")
                .getSingleResult();

        assertThat(findMember.getName()).isEqualTo("유저1");

    }

    @Test
    @DisplayName("passwordEncoder 구현로직 테스트")
    public void passwordEncoderTest() {

        Member findMember = em.createQuery("select distinct m from Member m " +
                "join fetch m.boards where m.accountId = :accountId", Member.class)
                .setParameter("accountId", "user1")
                .getSingleResult();


        assertThat(passwordEncoder.matches("1234", findMember.getAccountPw())).isTrue();
    }

    @Test
    public void test() {
        //given
        List<Member> members = em
                .createQuery("select m from Member m join fetch m.boards b", Member.class)
                .getResultList();
        //when

        BoardCommentForm boardCommentForm = new BoardCommentForm();
        boardCommentForm.setContent("두번째 댓글");
        BoardComment boardComment = BoardComment.createdBoardComment(boardCommentForm,
                members.get(0),
                members.get(0)
                        .getBoards()
                        .get(0));


        em.persist(boardComment);

        em.flush();
        em.clear();


        List<BoardComment> boardComments = em
                .createQuery("select bc from BoardComment bc", BoardComment.class)
                .getResultList();

        //then

        assertThat(boardComments)
                .extracting("content")
                .contains("두번째 댓글", "안녕하세요");

        assertThat(boardComments.get(0).getBoard().getContent()).isEqualTo("게시글 본문");
    }

}
