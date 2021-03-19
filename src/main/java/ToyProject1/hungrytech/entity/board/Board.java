package ToyProject1.hungrytech.entity.board;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.entity.BaseEntity;
import ToyProject1.hungrytech.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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


    public Board(BoardForm boardForm, Member member) {
        this.title = boardForm.getTitle();
        this.content = boardForm.getContent();
        this.imgPath = boardForm.getImgPath();
        insertMember(member);
    }

    public static Board createBoard(BoardForm boardForm, Member member) {
        return new Board(boardForm, member);
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
