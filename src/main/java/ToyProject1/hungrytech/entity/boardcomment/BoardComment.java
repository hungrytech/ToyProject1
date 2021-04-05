package ToyProject1.hungrytech.entity.boardcomment;

import ToyProject1.hungrytech.boardcommentDto.BoardCommentForm;
import ToyProject1.hungrytech.entity.BaseEntity;
import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "Comment_SEQ", sequenceName = "BoardComment_SEQ")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardComment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Comment_SEQ")
    @Column(name = "board_comment_id")
    private Long id;

    @Column(name = "board_comment_content")
    private String content;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    public BoardComment(String content, Member member, Board board) {
        this.content = content;
        this.member = member;
        insertBoard(board);

    }

    public static BoardComment createdBoardComment(BoardCommentForm commentForm,
                                                   Member member,
                                                   Board board){

        return new BoardComment(commentForm.getContent(), member, board);


    }


    private void insertBoard(Board board) {
        this.board = board;
        board.getBoardComments().add(this);
    }

    public void changeContent(String content){
        this.content = content;
    }
}
