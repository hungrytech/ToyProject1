package ToyProject1.hungrytech.boardDto;

import ToyProject1.hungrytech.entity.board.Board;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class BoardInfo {

    @Setter(value = AccessLevel.NONE)
    private Long id;

    private String title;

    private String content;

    private String imgPath;

    @Setter(value = AccessLevel.NONE)
    private LocalDateTime createdDate;

    public BoardInfo(Board board) {
        id = board.getId();
        title = board.getTitle();
        content = board.getContent();
        imgPath = board.getImgPath();
        createdDate = board.getCreatedDate();

    }
}
