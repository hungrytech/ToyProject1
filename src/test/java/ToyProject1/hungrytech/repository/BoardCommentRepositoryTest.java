package ToyProject1.hungrytech.repository;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.boardcommentDto.BoardCommentForm;
import ToyProject1.hungrytech.domain.board.Board;
import ToyProject1.hungrytech.domain.boardcomment.BoardComment;
import ToyProject1.hungrytech.domain.member.Member;
import ToyProject1.hungrytech.memberDto.MemberForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardCommentRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardCommentRepository boardCommentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void before() {
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

        memberRepository.save(member);

        //Board
        BoardForm boardForm1 = new BoardForm();
        boardForm1.setTitle("게시글 제목1");
        boardForm1.setContent("게시글 본문1");

        Board board1 = Board.createBoard(boardForm1, member);

        BoardForm boardForm2 = new BoardForm();
        boardForm2.setTitle("게시글 제목2");
        boardForm2.setContent("게시글 본문2");

        Board board2 = Board.createBoard(boardForm2, member);

        BoardForm boardForm3 = new BoardForm();
        boardForm3.setTitle("게시글 제목3");
        boardForm3.setContent("게시글 본문3");

        Board board3 = Board.createBoard(boardForm3, member);

        boardRepository.save(board1);
        boardRepository.save(board2);
        boardRepository.save(board3);
    }

    @Test
    @DisplayName("댓글 저장 테스트")
    void saveBoardComment() {

        //given
        List<Member> members = memberRepository.findAll();
        Member member = members.get(0);

        List<Board> boards = boardRepository.findAll();
        Board board = boards.get(0);


        BoardCommentForm commentForm = new BoardCommentForm();
        commentForm.setContent("이 댓글 저장 잘하네");

        //when
        BoardComment boardComment = BoardComment
                .createdBoardComment(commentForm, member, board);


        boardCommentRepository.save(boardComment);

        //then
        List<BoardComment> boardComments = boardCommentRepository.findAll();

        assertThat(boardComments)
                .extracting("content")
                .containsExactly("이 댓글 저장 잘하네");


    }

    @Test
    @DisplayName("댓글 저장 테스트 여러댓글 저장")
    void saveBoardComments() {

        //given
        List<Member> members = memberRepository.findAll();
        Member member = members.get(0);

        List<Board> boards = boardRepository.findAll();

        Board[] boardArray = boards.toArray(new Board[0]);

        int random = (int)(Math.random()*3);

        //when
        List<BoardComment> comments = new ArrayList<>();

        for(int i=0; i<20; i++) {

            BoardCommentForm boardCommentForm = new BoardCommentForm();
            boardCommentForm.setContent("댓글" + i);
            BoardComment boardComment = BoardComment
                    .createdBoardComment(boardCommentForm, member, boardArray[random]);

            comments.add(boardComment);

        }

        for (BoardComment boardComment : comments) {
            boardCommentRepository.save(boardComment);
        }

        List<BoardComment> result = boardCommentRepository.findAll();

        String[] commentContent = new String[20];

        for(int i=0; i<20; i++) {
            commentContent[i] = "댓글" + i;
        }

        //then

        assertThat(result.size()).isEqualTo(20);

        assertThat(result)
                .extracting("content")
                .containsExactly(commentContent);


    }




}