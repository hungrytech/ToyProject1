package ToyProject1.hungrytech.boardcommentDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BoardCommentForm {

    private String accountId;

    private String content;

    private Long boardId;


}
