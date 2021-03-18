package ToyProject1.hungrytech.repository;

import ToyProject1.hungrytech.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b join b.member m where m.accountId=:accountId")
    Page<Board> findBoardsToAccountId(@Param("accountId") String accountId, Pageable pageable);
}
