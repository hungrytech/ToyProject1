package ToyProject1.hungrytech.boardDto;

import ToyProject1.hungrytech.entity.board.Board;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class BulletinBoardInfo {
    private Long id;

    private String title;

    private String content;

    private String imgPath;

    private String memberAccountId;

    @Setter(value = AccessLevel.NONE)
    private LocalDateTime createdDate;

    public BulletinBoardInfo(Board board) {
        id = board.getId();
        title = board.getTitle();
        content = board.getContent();
        imgPath = board.getImgPath();
        createdDate = board.getCreatedDate();
        memberAccountId = board.getMember().getAccountId();

    }

}
