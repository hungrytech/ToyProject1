package ToyProject1.hungrytech.repository;

import ToyProject1.hungrytech.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface BoardRepository extends JpaRepository<Board, Long> {
    /**
     *게시물 검색
     *게시물 조회
     */
    //회원 아이디로 게시판 게시물 검색
    @Query(value = "select b from Board b join fetch b.member m where m.accountId=:accountId",
            countQuery = "select count(b) from Board b join b.member m where m.accountId=:accountId")
    Page<Board> pagingBoardsToAccountId(@Param("accountId") String accountId,
                                        Pageable pageable);

    //게시물 조회
    @Query(value = "select b from Board b join fetch b.member",
            countQuery = "select count(b) from Board b")
    Page<Board> pagingBoards(Pageable pageable);



    /**
     * 게시물 조회
     * 게시물 수정
     */
    //게시물 Id이용하여 작성한 member와 board조회
    @Query("select b from Board b join fetch b.member where b.id=:id")
    Board findBoardById(@Param("id")Long id);


    //회원 아이디 검색을 이용하여 해당 게시글 조회
    @Query("select b from Board b join b.member m " +
            "where m.accountId = :accountId and b.id = :id")
    Board findBoard(@Param("accountId") String accountId,
                          @Param("id") Long boardId);



}
