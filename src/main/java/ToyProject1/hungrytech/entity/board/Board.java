package ToyProject1.hungrytech.entity.board;

import ToyProject1.hungrytech.entity.BaseEntity;
import ToyProject1.hungrytech.entity.member.Member;
import lombok.Getter;

import javax.persistence.*;


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
    private String imgPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Board() {

    }

    public Board(String title, String content, String imgPath, Member member) {
        this.title = title;
        this.content = content;
        this.imgPath = imgPath;
        insertMember(member);
    }

    public static Board createBoard(String title, String content, String imgPath, Member member) {
        return new Board(title, content, imgPath, member);
    }

    private void insertMember(Member member) {
        this.member =member;
        member.getBoards().add(this);
    }

    /**
     * Board 제목, 게시글, 이미지 변경
     */
    public void changeTitle(String changeTitle) {
        title = changeTitle;
    }

    public void changeContent(String changeContent) {
        content = changeContent;
    }

    public void changeImg(String imgPath) {
        this.imgPath = imgPath;
    }






}
