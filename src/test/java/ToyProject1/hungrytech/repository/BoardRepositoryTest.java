package ToyProject1.hungrytech.repository;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.memberDto.MemberForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class BoardRepositoryTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

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

        memberRepository.save(member);

        //Board
        BoardForm boardForm1 = new BoardForm();
        boardForm1.setTitle("게시글 제목1");
        boardForm1.setContent("게시글 본문1");
        boardForm1.setImgPath("C:");

        Board board1 = Board.createBoard(boardForm1, member);

        BoardForm boardForm2 = new BoardForm();
        boardForm2.setTitle("게시글 제목2");
        boardForm2.setContent("게시글 본문2");
        boardForm2.setImgPath("C:");

        Board board2 = Board.createBoard(boardForm2, member);

        BoardForm boardForm3 = new BoardForm();
        boardForm3.setTitle("게시글 제목3");
        boardForm3.setContent("게시글 본문3");
        boardForm3.setImgPath("C:");

        Board board3 = Board.createBoard(boardForm3, member);

        boardRepository.save(board1);
        boardRepository.save(board2);
        boardRepository.save(board3);
    }




    @Test
    @DisplayName("board title 변경 테스트")
    public void BoardTitleChange() {
        //given


        List<Board> boards = boardRepository.findAll();

        Board board = boards.get(0);

        //when
        Optional<Board> boardOptional = boardRepository.findById(board.getId());
        Board findBoard = null;

        if(boardOptional.isPresent()) {
            findBoard = boardOptional.get();
        }

        findBoard.changeTitle("게시글 변경한 제목");

        //then
        assertThat(board.getTitle()).isEqualTo("게시글 변경한 제목");


    }

    @Test
    @DisplayName("해당 회원이 작성한 게시물을 페이징 하여 가져온다")
    public void PageingBoards(){
        //given
        List<Board> boards = boardRepository.findAll();

        Member member = boards.get(0).getMember();

        //when
        PageRequest pageRequest = PageRequest.of(0,2, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Board> page = boardRepository.findBoardsToAccountId(member.getAccountId(), pageRequest);

        //then

        assertThat(page.getTotalPages()).isEqualTo(2);

        assertThat(page.getTotalElements()).isEqualTo(3);

        assertThat(page.isFirst()).isTrue();

        assertThat(page.hasNext()).isTrue();

        assertThat(page.getTotalPages()).isEqualTo(2);

    }

    @Test
    @DisplayName("해당 회원이 쓴 해당 게시글 조회")
    public void test() {
        //given
        Member member = memberRepository.findMemberByAccountId("user1");
        List<Board> boards = boardRepository.findAll();

        Board board = boardRepository.findBoard(member.getAccountId(), boards.get(0).getId());

        List<Board> memberBoards = member.getBoards();
        assertThat(board).isSameAs(memberBoards.get(0));


    }



}
