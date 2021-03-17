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
        Member member = Member.createMember("userD",
                passwordEncoder.encode("abcd1234"),
                "userD@gmail.com",
                "010-1111-1111");
        memberRepository.save(member);

        Board board = Board.createBoard("게시글 이름",
                "게시글 내용입니다.",
                "C:", member);

        boardRepository.save(board);

        //when


    }


}
