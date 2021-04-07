package ToyProject1.hungrytech.service.board;

import ToyProject1.hungrytech.boardcommentDto.BoardCommentForm;
import ToyProject1.hungrytech.entity.boardcomment.BoardComment;

public interface BoardCommentService {

    /**
     * 댓글 등록, 수정, 삭제
     *
     */
    BoardComment writeComment(BoardCommentForm boardCommentForm);

    void updateCommentContent(Long boardCommentId, String content);

    void deleteComment(Long boardCommentId);


}
