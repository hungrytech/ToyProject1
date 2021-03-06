package ToyProject1.hungrytech.controller;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.boardDto.BoardInfo;
import ToyProject1.hungrytech.boardDto.BoardSearchCondition;
import ToyProject1.hungrytech.boardDto.BulletinBoardInfo;
import ToyProject1.hungrytech.boardcommentDto.BoardCommentInfo;
import ToyProject1.hungrytech.config.LoginMember;
import ToyProject1.hungrytech.domain.board.Board;
import ToyProject1.hungrytech.domain.boardcomment.BoardComment;
import ToyProject1.hungrytech.memberDto.MemberLoginInfo;
import ToyProject1.hungrytech.service.board.BoardService;
import ToyProject1.hungrytech.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;

    /**
     *게시글 작성
     * 게시글 조회
     * 게시글 수정
     * 게시글 삭제
     */
    //게시글 작성
    @GetMapping("/board/new")
    public String boardForm(BoardForm boardForm, Model model) {

        model.addAttribute("boardForm", boardForm);
        return "board/writeBoard/board_form";
    }

    @PostMapping("/board/new")
    @ResponseStatus(HttpStatus.CREATED)
    public String boardWrite(@RequestPart("file") MultipartFile file,
                             BoardForm boardForm,
                             @LoginMember MemberLoginInfo loginInfo) throws Exception {

        if(!file.isEmpty()) {
            String boardImgPath = fileService.fileUpload(file);

            //업로드 후 해당 파일경로를 저장
            boardForm.setImgPath(boardImgPath);
        }

        boardService.writeBoard(boardForm, loginInfo.getAccountId());

        return "board/writeBoard/board_write_success";
    }

    //게시글 조회
    @GetMapping("/board/{id}")
    public String findBoard(@PathVariable("id")Long boardId,
                            @LoginMember MemberLoginInfo loginInfo,
                            Model model)  {

        Board findBoard = boardService.findBoardById(boardId);

        List<BoardCommentInfo> commentInfoList = changeBoardCommentInfo(findBoard.getBoardComments());

        //로그인 되었을경우
        if(loginInfo != null) {

            //자기 게시물인지 확인
            boolean result = personalPublication(
                    findBoard.getMember().getAccountId(),
                    loginInfo.getAccountId());

            model.addAttribute("boardInfo", new BoardInfo(findBoard));
            model.addAttribute("result", result);
            model.addAttribute("comments", commentInfoList);
            return "board/inquireBoard/inquire_board";
        }

        //로그인이 안되있을경우
        model.addAttribute("boardInfo", new BoardInfo(findBoard));
        model.addAttribute("result", false);
        model.addAttribute("comments", commentInfoList);
        return "board/inquireBoard/inquire_board";
    }

    private List<BoardCommentInfo> changeBoardCommentInfo(List<BoardComment> comments) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        Collections.reverse(comments);

        return comments
                .stream()
                .map(c -> new BoardCommentInfo(c, c.getCreatedDate().format(dateTimeFormatter)))
                .collect(Collectors.toList());
    }

    private boolean personalPublication(String memberAccountId, String sessionAccountId) {
        return memberAccountId.equals(sessionAccountId);
    }


    //게시글 수정
    @PostMapping("/board/{id}/edit")
    public String editBoard(@PathVariable("id") Long boardId,
                            @RequestPart("file") MultipartFile file,
                            @LoginMember MemberLoginInfo loginInfo,
                            BoardInfo boardInfo) throws Exception {


        Board findBoard = boardService.findBoardById(boardId);

        boardInfo.setId(findBoard.getId());
        boardInfo.setImgPath(findBoard.getImgPath());

        boolean result = fileService.deleteFile(boardInfo.getImgPath());


        //기존 게시글에 이미지파일이 있는경우
        if(result) {

            //수정된 이미지파일이 있을경우
            if(!file.isEmpty()) {
                String boardImgPath = fileService.fileUpload(file);

                boardInfo.setImgPath(boardImgPath);

                boardService.changeBoard(boardInfo,loginInfo.getAccountId());

                return "board/boardChange/board_info_change";
            }


            boardInfo.setImgPath(null);
            boardService.changeBoard(boardInfo, loginInfo.getAccountId());

            return "board/boardChange/board_info_change";
        }

        //기존 게시물에 이미지파일이 없는경우
        if(!file.isEmpty()) {
            String boardImgPath = fileService.fileUpload(file);

            boardInfo.setImgPath(boardImgPath);

            boardService.changeBoard(boardInfo, loginInfo.getAccountId());

            return "board/boardChange/board_info_change";

        }

        boardService.changeBoard(boardInfo, loginInfo.getAccountId());

        return "board/boardChange/board_info_change";
    }

    //게시글 삭제
    @PostMapping("/board/{id}/delete")
    public String deleteBoard(@PathVariable("id")Long boardId)  {

        Board findBoard = boardService.findBoardById(boardId);

        fileService.deleteFile(findBoard.getImgPath());

        boardService.deletedBoard(findBoard.getId());

        return "board/boardDelete/board_delete";
    }

    /**
     * 게시판 (Spring Data JPA 정적쿼리)
     */
    /*@GetMapping("/boards")
    public String bulletinBoard(Pageable pageable,
                                Model model) {

        Page<Board> boardPage = boardService.getBoardList(pageable);


        model.addAttribute("boardPage", boardPage);
        model.addAttribute("boards", getBulletinBoardInfoList(boardPage));
        return "board/bulletinBoard/bulletinBoard";
    }

    private List<BulletinBoardInfo> getBulletinBoardInfoList(Page<Board> boardPage) {
        return boardPage
                .getContent()
                .stream()
                .map(BulletinBoardInfo::new)
                .collect(Collectors.toList());
    }*/

    /**
     * 게시판 (Querydsl을 이용한 동적쿼리)
     * 검색기능 추가
     */
    @GetMapping("/boards")
    public String bulletinBoard(Pageable pageable,
                                @ModelAttribute("searchCondition")
                                        BoardSearchCondition searchCondition,
                                Model model) {

        Page<BulletinBoardInfo> boardPage = boardService.searchBoardList(pageable, searchCondition);

        model.addAttribute("boardPage", boardPage);
        model.addAttribute("boards", boardPage.getContent());
        return "board/bulletinBoard/bulletin_board";
    }



    @GetMapping("/boards/write")
    public String bulletinBoardWrite(@LoginMember MemberLoginInfo loginInfo) {

        //로그인 되있을경우
        if(loginInfo != null) {
           return "redirect:/board/new";
        }

        //로그인되어있지 않을경우
        return "boardErrorPage/request_login";

    }
}
