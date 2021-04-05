package ToyProject1.hungrytech.repository;

import ToyProject1.hungrytech.entity.boardcomment.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {

    @Query("select c from BoardComment c where c.id =:boardCommentId")
    BoardComment findBoardCommentById(@Param("boardCommentId")Long boardCommentId);
}
