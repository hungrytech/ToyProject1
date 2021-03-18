package ToyProject1.hungrytech.repository;

import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.memberDto.MemberForm;
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

    @Test
    @DisplayName("board title 변경 테스트")
    public void BoardTitleChange() {
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

        Board board = Board.createBoard("게시글 제목",
                "게시글 내용입니다.",
                "C:", member);

        boardRepository.save(board);

        Optional<Board> boardOptional = boardRepository.findById(board.getId());
        Board findBoard = null;

        if(boardOptional.isPresent()) {
            findBoard = boardOptional.get();
        }

        findBoard.changeTitle("게시글 변경한 제목");
        //when
        assertThat(board.getTitle()).isEqualTo("게시글 변경한 제목");


    }

    @Test
    @DisplayName("해당 회원이 작성한 게시물을 페이징 하여 가져온다")
    public void PageingBoards(){
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

        memberRepository.save(member);

        Board board1 = Board.createBoard("게시물 제목1",
                "게시글 본문1",
                null,
                member);

        boardRepository.save(board1);

        Board board2 = Board.createBoard("게시물 제목2",
                "게시글 본문2",
                null,
                member);

        boardRepository.save(board2);
        Board board3 = Board.createBoard("게시물 제목3",
                "게시글 본문3",
                null,
                member);

        boardRepository.save(board3);
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


}
