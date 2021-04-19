package ToyProject1.hungrytech.service.board;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.boardDto.BoardInfo;
import ToyProject1.hungrytech.boardDto.BoardSearchCondition;
import ToyProject1.hungrytech.boardDto.BulletinBoardInfo;
import ToyProject1.hungrytech.boardcommentDto.BoardCommentForm;
import ToyProject1.hungrytech.domain.board.Board;
import ToyProject1.hungrytech.domain.boardcomment.BoardComment;
import ToyProject1.hungrytech.domain.member.Member;
import ToyProject1.hungrytech.repository.BoardCommentRepository;
import ToyProject1.hungrytech.repository.BoardRepository;
import ToyProject1.hungrytech.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardJpaService implements BoardService {

    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    /**
     * 게시글 작성
     * 게시글 수정
     * 게시글 삭제
     */
    //게시글 작성
    @Override
    @Transactional
    public void writeBoard(BoardForm boardForm, String accountId ){
        Member findMember = memberRepository.findMemberByAccountId(accountId);
        Board board = Board.createBoard(boardForm, findMember);
        boardRepository.save(board);
    }

    //게시글 수정
    @Override
    @Transactional
    public void changeBoard(BoardInfo boardInfo, String accountId) {

        Board board = boardRepository
                .findBoard(accountId,
                        boardInfo.getId());

        board.changeTitle(boardInfo.getTitle());
        board.changeContent(boardInfo.getContent());
        board.changeImg(boardInfo.getImgPath());

    }

    //게시글 삭제
    @Override
    @Transactional
    public void deletedBoard(Long boardId) {
        Board findBoard = boardRepository
                .findBoardById(boardId);

        boardRepository.delete(findBoard);

    }
    /**
     * 게시글 단건 조회
     */
    @Override
    public Board findBoardById(Long boardId) {
        return boardRepository.findBoardById(boardId);
    }

    /**
     * 게시글 조회
     * 사용자 아이디를 이용한 게시글 조회
     */
    @Override
    public Page<Board> getBoardList(Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdDate"));

        return boardRepository.pagingBoards(pageable);
    }

    @Override
    public Page<Board> getBoardListByAccountId(String accountId, Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
        pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdDate"));

        return boardRepository.pagingBoardsToAccountId(accountId, pageable);
    }

    /**
     * 게시글 조회,
     * 제목, 내용, 해당게시글 작성자아이디, 제목+내용 검색
     */
    @Override
    public Page<BulletinBoardInfo> searchBoardList(Pageable pageable, BoardSearchCondition searchCondition) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
        pageable = PageRequest.of(page, 10);

        return boardRepository.search(pageable, searchCondition);

    }

    /**
     * 댓글 등록, 수정, 삭제
     */
    @Override
    @Transactional
    public BoardComment writeComment(BoardCommentForm commentForm) {

        Member findMember = memberRepository.findMemberByAccountId(commentForm.getAccountId());

        Board findBoard = boardRepository.findBoardById(commentForm.getBoardId());

        BoardComment boardComment = BoardComment
                .createdBoardComment(commentForm, findMember, findBoard);

        return boardCommentRepository.save(boardComment);

    }

    @Override
    @Transactional
    public void updateCommentContent(Long boardCommentId, String content) {
        BoardComment boardComment = boardCommentRepository.findBoardCommentById(boardCommentId);

        boardComment.changeContent(content);

    }

    @Override
    @Transactional
    public void deleteComment(Long boardCommentId) {
        BoardComment boardComment = boardCommentRepository.findBoardCommentById(boardCommentId);
        boardCommentRepository.delete(boardComment);
    }
}
