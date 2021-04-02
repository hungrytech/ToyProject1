package ToyProject1.hungrytech.controller;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.boardDto.BoardInfo;
import ToyProject1.hungrytech.boardDto.BoardSearchCondition;
import ToyProject1.hungrytech.boardDto.BulletinBoardInfo;
import ToyProject1.hungrytech.entity.board.Board;
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


import javax.servlet.http.HttpSession;
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
        return "board/writeBoard/boardForm";
    }

    @PostMapping("/board/new")
    @ResponseStatus(HttpStatus.CREATED)
    public String boardWrite(@RequestPart("file") MultipartFile file,
                             BoardForm boardForm,
                             HttpSession session) throws Exception {


        MemberLoginInfo loginInfo = (MemberLoginInfo)session
                .getAttribute("memberInfo");

        if(!file.isEmpty()) {
            String boardImgPath = fileService.fileUpload(file);

            //업로드 후 해당 파일경로를 저장
            boardForm.setImgPath(boardImgPath);
        }

        boardService.writeBoard(boardForm, loginInfo.getAccountId());

        return "home";
    }

    //게시글 조회
    @GetMapping("/board/{id}")
    public String findBoard(@PathVariable("id")Long boardId,
                            Model model,
                            HttpSession session)  {

        Board findBoard = boardService.findBoardById(boardId);

        //로그인 되었을경우
        if(session.getAttribute("memberInfo") != null) {
            MemberLoginInfo memberInfo = (MemberLoginInfo) session
                    .getAttribute("memberInfo");

            //자기 게시물인지 확인
            boolean result = personalPublication(
                    findBoard.getMember().getAccountId(),
                    memberInfo.getAccountId());

            model.addAttribute("boardInfo", new BoardInfo(findBoard));
            model.addAttribute("result", result);
            return "board/inquireBoard/inquireBoard";
        }

        //로그인이 안되있을경우
        model.addAttribute("boardInfo", new BoardInfo(findBoard));
        model.addAttribute("result", false);
        return "board/inquireBoard/inquireBoard";
    }

    private boolean personalPublication(String memberAccountId, String sessionAccountId) {
        return memberAccountId.equals(sessionAccountId);
    }


    //게시글 수정
    @PostMapping("/board/{id}/edit")
    public String editBoard(@PathVariable("id") Long boardId,
                            @RequestPart("file") MultipartFile file,
                            HttpSession session,
                            BoardInfo boardInfo) throws Exception {

        MemberLoginInfo loginInfo = (MemberLoginInfo) session
                .getAttribute("memberInfo");

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

                return "board/boardChange/boardInfoChange";
            }


            boardInfo.setImgPath(null);
            boardService.changeBoard(boardInfo, loginInfo.getAccountId());

            return "board/boardChange/boardInfoChange";
        }

        //기존 게시물에 이미지파일이 없는경우
        if(!file.isEmpty()) {
            String boardImgPath = fileService.fileUpload(file);

            boardInfo.setImgPath(boardImgPath);

            boardService.changeBoard(boardInfo, loginInfo.getAccountId());

            return "board/boardChange/boardInfoChange";

        }

        boardService.changeBoard(boardInfo, loginInfo.getAccountId());

        return "board/boardChange/boardInfoChange";
    }

    //게시글 삭제
    @PostMapping("/board/{id}/delete")
    public String deleteBoard(@PathVariable("id")Long boardId)  {
        Board findBoard = boardService.findBoardById(boardId);

        fileService.deleteFile(findBoard.getImgPath());

        boardService.deletedBoard(findBoard.getId());

        return "board/boardDelete/boardDelete";
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
                                @ModelAttribute("searchCondition") BoardSearchCondition searchCondition,
                                Model model) {

        Page<BulletinBoardInfo> boardPage = boardService.searchBoardList(pageable, searchCondition);

        model.addAttribute("boardPage", boardPage);
        model.addAttribute("boards", boardPage.getContent());
        return "board/bulletinBoard/bulletinBoard";
    }



    @GetMapping("/boards/write")
    public String bulletinBoardWrite(HttpSession session) {
        //로그인 되있을경우
        if(session.getAttribute("memberInfo") != null) {
           return "redirect:/board/new";
        }

        //로그인되어있지 않을경우
        return "boardErrorPage/requestLogin";

    }
}
