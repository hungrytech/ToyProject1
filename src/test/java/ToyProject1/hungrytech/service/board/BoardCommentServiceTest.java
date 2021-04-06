package ToyProject1.hungrytech.service.board;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.boardcommentDto.BoardCommentForm;
import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.boardcomment.BoardComment;
import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.memberDto.MemberForm;
import ToyProject1.hungrytech.service.member.MemberService;
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
class BoardCommentServiceTest {

    @Autowired
    BoardCommentService boardCommentService;

    @Autowired
    BoardService boardService;

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void before() {
        //Member
        MemberForm memberForm1 = new MemberForm();
        memberForm1.setAccountId("user1");
        memberForm1.setAccountPw(passwordEncoder.encode("1234"));
        memberForm1.setPhoneNumber("010-1111-1111");
        memberForm1.setEmail("user1@gmail.com");
        memberForm1.setName("user1");


        //Board
        BoardForm boardForm1 = new BoardForm();
        boardForm1.setTitle("게시글 제목1");
        boardForm1.setContent("게시글 본문1");

        BoardForm boardForm2 = new BoardForm();
        boardForm2.setTitle("게시글 제목2");
        boardForm2.setContent("게시글 본문2");

        BoardForm boardForm3 = new BoardForm();
        boardForm3.setTitle("게시글 제목3");
        boardForm3.setContent("게시글 본문3");

        //회원저장
        memberService.join(memberForm1);

        String accountId = memberForm1.getAccountId();

        //게시글 작성
        boardService.writeBoard(boardForm1, accountId);
        boardService.writeBoard(boardForm2, accountId);
        boardService.writeBoard(boardForm3, accountId);

    }

    @Test
    @DisplayName("댓글 저장 테스트")
    void saveComment() {
        //given
        Member member = memberService.findInfo("user1");
        List<Board> boards = member.getBoards();
        List<BoardCommentForm> commentForms = new ArrayList<>();
        for(int i=0; i<10; i++) {
            BoardCommentForm commentForm = new BoardCommentForm();
            commentForm.setContent("댓글" + i);
            commentForm.setBoardId(boards.get(0).getId());
            commentForm.setAccountId(member.getAccountId());
            commentForms.add(commentForm);

        }


        //when

        for (BoardCommentForm commentForm : commentForms) {
            boardCommentService.writeComment(commentForm);

        }


        //then
        List<BoardComment> boardComments = boards.get(0).getBoardComments();

        assertThat(boardComments).isNotNull();

        assertThat(boardComments.size()).isEqualTo(10);


    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void updateComment(){
        Member member = memberService.findInfo("user1");
        List<Board> boards = member.getBoards();

        List<BoardCommentForm> commentForms = new ArrayList<>();
        for(int i=0; i<10; i++) {
            BoardCommentForm commentForm = new BoardCommentForm();
            commentForm.setContent("댓글" + i);
            commentForm.setBoardId(boards.get(0).getId());
            commentForm.setAccountId(member.getAccountId());
            commentForms.add(commentForm);

        }

        for (BoardCommentForm commentForm : commentForms) {
            boardCommentService.writeComment(commentForm);

        }

        //when

        List<BoardComment> boardComments = boards.get(0).getBoardComments();

        BoardComment boardComment = boardComments.get(2);

        boardCommentService.updateCommentContent(boardComment.getId(), "변경된 댓글");

        //then
        assertThat(boardComments)
                .extracting("content")
                .contains("변경된 댓글");



    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteComment(){
        Member member = memberService.findInfo("user1");
        List<Board> boards = member.getBoards();

        List<BoardCommentForm> commentForms = new ArrayList<>();
        for(int i=0; i<10; i++) {
            BoardCommentForm commentForm = new BoardCommentForm();
            commentForm.setContent("댓글" + i);
            commentForm.setBoardId(boards.get(0).getId());
            commentForm.setAccountId(member.getAccountId());
            commentForms.add(commentForm);

        }

        for (BoardCommentForm commentForm : commentForms) {
            boardCommentService.writeComment(commentForm);

        }

        //when

        List<BoardComment> boardComments = boards.get(0).getBoardComments();

        BoardComment boardComment = boardComments.get(2);

        boardCommentService.deleteComment(boardComment.getId());



        //then
        //flush, clear를 해야 영속성 컨텍스트에서 가져오지 않고, 적용된 db에서 가져온다.
        assertThat(boardComments.size()).isEqualTo(10);


    }




}