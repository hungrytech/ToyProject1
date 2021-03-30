package ToyProject1.hungrytech.service.board;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.boardDto.BoardInfo;
import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.repository.BoardRepository;
import ToyProject1.hungrytech.service.member.MemberService;
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

    private final BoardRepository boardRepository;
    private final MemberService memberService;

    /**
     * 게시글 작성
     * 게시글 수정
     * 게시글 삭제
     */
    //게시글 작성
    @Transactional
    @Override
    public void writeBoard(BoardForm boardForm, String accountId ){
        Member findMember = memberService.findInfo(accountId);
        Board board = Board.createBoard(boardForm, findMember);
        boardRepository.save(board);
    }

    //게시글 수정
    @Transactional
    @Override
    public void changeBoard(BoardInfo boardInfo, String accountId) {

        Board board = boardRepository
                .findBoard(accountId,
                        boardInfo.getId());

        board.changeTitle(boardInfo.getTitle());
        board.changeContent(boardInfo.getContent());
        board.changeImg(boardInfo.getImgPath());

    }

    //게시글 삭제
    @Transactional
    @Override
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
}
