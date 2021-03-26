package ToyProject1.hungrytech.service.board;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.boardDto.BoardInfo;
import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.entity.member.Member;
import ToyProject1.hungrytech.repository.BoardRepository;
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

    /**
     * 게시글 작성
     * 게시글 수정
     * 게시글 삭제
     */
    //게시글 작성
    @Transactional
    @Override
    public void writeBoard(BoardForm boardForm, Member member){
        Board board = Board.createBoard(boardForm, member);
        boardRepository.save(board);
    }

    //게시글 수정
    @Transactional
    @Override
    public void changeBoard(BoardInfo boardInfo, Member member) {

        Board board = boardRepository
                .findBoard(member.getAccountId(),
                        boardInfo.getId());

        board.changeTitle(boardInfo.getTitle());
        board.changeContent(boardInfo.getContent());
        board.changeImg(boardInfo.getImgPath());

    }

    //게시글 삭제
    @Transactional
    @Override
    public void deletedBoard(BoardInfo boardInfo) {
        Board findBoard = boardRepository
                .findBoardById(boardInfo.getId());

        boardRepository.delete(findBoard);

    }

    /**
     * 게시글 조회
     * 사용자 아이디를 이용한 게시글 조회
     */
    @Override
    public Page<Board> getBoardList(Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
        pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdDate"));

        return boardRepository.findAll(pageable);
    }

    @Override
    public Page<Board> getBoardListByAccountId(String accountId, Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
        pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdDate"));

        return boardRepository.findBoardsToAccountId(accountId, pageable);
    }
}
