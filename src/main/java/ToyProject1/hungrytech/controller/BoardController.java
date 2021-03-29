package ToyProject1.hungrytech.controller;

import ToyProject1.hungrytech.boardDto.BoardForm;
import ToyProject1.hungrytech.boardDto.BoardInfo;
import ToyProject1.hungrytech.entity.board.Board;
import ToyProject1.hungrytech.memberDto.MemberLoginInfo;
import ToyProject1.hungrytech.service.board.BoardService;
import ToyProject1.hungrytech.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpSession;


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

        MemberLoginInfo memberInfo = (MemberLoginInfo)session
                .getAttribute("memberInfo");

        //자기 게시물인지 확인
        boolean result = personalPublication(
                findBoard.getMember().getAccountId(),
                memberInfo.getAccountId());

        model.addAttribute("boardInfo", new BoardInfo(findBoard));
        model.addAttribute("result", result);

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

            if(!file.isEmpty()) {
                String boardImgPath = fileService.fileUpload(file);

                boardInfo.setImgPath(boardImgPath);

                boardService.changeBoard(boardInfo,loginInfo.getAccountId());

                return "board/boardChange/boardInfoChange";
            }

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
}
