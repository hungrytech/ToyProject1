package ToyProject1.hungrytech.service.board;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.boardDto.BoardInfo;
import ToyProject1.hungrytech.boardDto.BoardSearchCondition;
import ToyProject1.hungrytech.boardDto.BulletinBoardInfo;
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
    void writeBoard(BoardForm boardForm, String accountId);

    //게시글 수정
    void changeBoard(BoardInfo boardInfo, String accountId);

    //게시글 삭제
    void deletedBoard(Long boardId);


    /**
     * 게시글 단건 조회
     */
    Board findBoardById(Long boardId);



    /**
     * 게시글 조회
     * 사용자 아이디를 이용한 게시글 조회
     */
    Page<Board> getBoardList(Pageable pageable);

    Page<Board> getBoardListByAccountId(String accountId, Pageable pageable);


    /**
     * 게시글 조회,
     * 제목, 내용, 해당게시글 작성자아이디, 제목+내용 검색
     */
    Page<BulletinBoardInfo> searchBoardList(Pageable pageable, BoardSearchCondition searchCondition);

}

