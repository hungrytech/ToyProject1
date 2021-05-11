package ToyProject1.hungrytech.domain.boardcomment;

import ToyProject1.hungrytech.boardcommentDto.BoardCommentForm;
import ToyProject1.hungrytech.domain.BaseEntity;
import ToyProject1.hungrytech.domain.board.Board;
import ToyProject1.hungrytech.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardComment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        insertMember(member);
        insertBoard(board);

    }

    public static BoardComment createdBoardComment(BoardCommentForm commentForm,
                                                   Member member,
                                                   Board board){

        return new BoardComment(commentForm.getContent(), member, board);


    }

    private void insertMember(Member member) {
        this.member = member;
        member.getBoardComments().add(this);
    }

    private void insertBoard(Board board) {
        this.board = board;
        board.getBoardComments().add(this);
    }

    public void changeContent(String content){
        this.content = content;
    }
}
