package ToyProject1.hungrytech.entity.board;

import ToyProject1.hungrytech.entity.BaseEntity;
import ToyProject1.hungrytech.entity.member.Member;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
public class Board extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_title", nullable = false)
    private String title;

    @Lob
    @Column(name = "board_content", nullable = false)
    private String content;

    @Column(name = "board_img")
    private String img;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Board() {

    }

    public Board(String title, String content, String img) {
        this.title = title;
        this.content = content;
        this.img = img;
    }

    public static Board createBoard(String title, String content, String img) {
        return new Board(title, content, img);
    }

    public void insertMember(Member member) {
        this.member =member;
        member.getBoards().add(this);
    }


}
