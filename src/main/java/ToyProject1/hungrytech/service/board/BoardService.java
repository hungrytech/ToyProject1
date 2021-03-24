package ToyProject1.hungrytech.service.board;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.boardDto.BoardInfo;
import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BoardService {

    /**
     * 게시글 작성
     * 게시글 수정
     * 게시글 삭제
     */
    //게시글 작성
    void writeBoard(BoardForm boardForm, Member member);

    //게시글 수정
    void changeBoard(BoardInfo boardInfo, Member member);

    //게시글 삭제
    void deletedBoard(BoardInfo boardInfo);

    /**
     * 게시글 조회
     * 사용자 아이디를 이용한 게시글 조회
     */
    Page<Board> getBoardList(Pageable pageable);

    Page<Board> getBoardListByAccountId(String accountId, Pageable pageable);
}
