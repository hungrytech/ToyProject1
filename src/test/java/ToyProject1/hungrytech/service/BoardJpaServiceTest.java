package ToyProject1.hungrytech.service;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.boardDto.BoardInfo;
import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.memberDto.MemberForm;
import ToyProject1.hungrytech.repository.BoardRepository;
import ToyProject1.hungrytech.service.board.BoardService;
import ToyProject1.hungrytech.service.member.MemberJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class BoardJpaServiceTest {
    @Autowired
    MemberJpaService memberJpaService;

    @Autowired
    BoardService boardService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    BoardRepository boardRepository;


    @BeforeEach
    @DisplayName("게시글 작성")
    public void before() {
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
        boardForm1.setImgPath("C:");

        BoardForm boardForm2 = new BoardForm();
        boardForm2.setTitle("게시글 제목2");
        boardForm2.setContent("게시글 본문2");
        boardForm2.setImgPath("C:");


        BoardForm boardForm3 = new BoardForm();
        boardForm3.setTitle("게시글 제목3");
        boardForm3.setContent("게시글 본문3");
        boardForm3.setImgPath("C:");



        //회원저장
        memberJpaService.join(memberForm1);

        Member findMember = memberJpaService.findInfo(memberForm1.getAccountId());


        //게시글 작성
        boardService.writeBoard(boardForm1, findMember);
        boardService.writeBoard(boardForm2, findMember);
        boardService.writeBoard(boardForm3, findMember);

    }

    @Test
    @DisplayName("게시글 작성 테스트")
    public void writeBoard() {
        //given
        Member member = memberJpaService.findInfo("user1");

        BoardForm boardForm = new BoardForm();
        boardForm.setTitle("제목1");
        boardForm.setContent("본문1");
        boardForm.setImgPath("C:");

        //when
        boardService.writeBoard(boardForm, member);

        List<Board> boards = boardRepository.findAll();


        //then
        assertThat(boards.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void boardChange() {
        //given
        Member member = memberJpaService.findInfo("user1");
        List<Board> boards = boardRepository.findAll();

        List<BoardInfo> boardInfos = boards.stream()
                .map(BoardInfo::new)
                .collect(Collectors.toList());



        //when
        BoardInfo boardInfo = boardInfos.get(0);
        boardInfo.setContent("바뀐 본문");
        boardInfo.setTitle("바뀐 제목");
        boardInfo.setImgPath("바뀐 이미지 경로");



        boardService.changeBoard(boardInfo, member);

        //then
        List<Board> result = boardRepository.findAll();
        assertThat(result.get(0).getContent()).isEqualTo("바뀐 본문");


    }

    @Test
    @DisplayName("게시물 삭제 테스트")
    public void deletedBoard() {
        //given
        List<Board> boards = boardRepository.findAll();

        List<BoardInfo> boardInfos = boards.stream()
                .map(BoardInfo::new)
                .collect(Collectors.toList());
        //when
        for (BoardInfo boardInfo : boardInfos) {
            boardService.deletedBoard(boardInfo);
        }

        List<Board> result = boardRepository.findAll();
        //then
        assertThat(result.size()).isEqualTo(0);
    }


    @Test
    @DisplayName("게시판 게시글 조회 테스트")
    public void boardList() {
        //given
        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<Board> page = boardService.getBoardList(pageable);

        List<Board> content = page.getContent();



        //then
        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.hasNext()).isFalse();
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);


    }

    @Test
    @DisplayName("해당 회원 아이디를 이용한 게시글 조회 테스트")
    public void test() {
        //given
        String accountId = "user1";


        //when
        Page<Board> page = boardService
                .getBoardListByAccountId(accountId,
                        PageRequest.of(0, 10));

        //then
        assertThat(page.getContent().size()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.hasNext()).isFalse();
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getContent()
                .get(1)
                .getMember()
                .getAccountId())
                .isEqualTo(accountId);


    }


}