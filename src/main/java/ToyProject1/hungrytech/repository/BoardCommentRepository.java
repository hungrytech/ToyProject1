package ToyProject1.hungrytech.repository;

import ToyProject1.hungrytech.entity.boardcomment.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {


}
