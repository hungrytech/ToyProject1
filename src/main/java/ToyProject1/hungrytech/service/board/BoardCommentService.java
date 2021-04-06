package ToyProject1.hungrytech.service.board;

import ToyProject1.hungrytech.boardcommentDto.BoardCommentForm;

public interface BoardCommentService {

    /**
     * 댓글 등록, 수정, 삭제
     *
     */
    void writeComment(BoardCommentForm boardCommentForm);

    void updateCommentContent(Long boardCommentId, String content);

    void deleteComment(Long boardCommentId);


}
