package ToyProject1.hungrytech.boardcommentDto;

import ToyProject1.hungrytech.entity.boardcomment.BoardComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
public class BoardCommentInfo {

    private String accountId;
    private String content;
    private Long boardId;
    private Long boardCommentId;
    private String dateTime;

    public BoardCommentInfo(BoardComment comment, String dateTime){
        accountId = comment.getMember().getAccountId();
        content = comment.getContent();
        boardId = comment.getBoard().getId();
        boardCommentId = comment.getId();
        this.dateTime = dateTime;

    }

}
