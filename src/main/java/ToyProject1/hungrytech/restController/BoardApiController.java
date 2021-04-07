package ToyProject1.hungrytech.restController;

import ToyProject1.hungrytech.boardcommentDto.BoardCommentForm;
import ToyProject1.hungrytech.boardcommentDto.BoardCommentInfo;
import ToyProject1.hungrytech.entity.boardcomment.BoardComment;
import ToyProject1.hungrytech.service.board.BoardService;
import ToyProject1.hungrytech.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class BoardApiController {
    private final BoardService boardService;
    private final FileService fileService;

    @GetMapping("/loadImgFile/{imgPath}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public byte[] loadImgFile(@PathVariable("imgPath") String imgPath) throws Exception {

        String localPath = fileService.changeLocalPath(imgPath);

        FileInputStream fileInputStream = new FileInputStream(localPath);

        byte[] imgBytes = IOUtils.toByteArray(fileInputStream);

        fileInputStream.close();

        return imgBytes;

    }

    @PostMapping("/boardComment/new")
    public ResponseEntity<BoardCommentInfo> writeComment(@RequestBody(required = false)
                                                                     BoardCommentForm commentForm) {

        if(commentForm.getBoardId() == null || commentForm.getAccountId()==null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        BoardComment boardComment = boardService.writeComment(commentForm);

        LocalDateTime createdDate = boardComment.getCreatedDate();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        BoardCommentInfo boardCommentInfo =
                new BoardCommentInfo(
                        boardComment,
                        createdDate.format(dateTimeFormatter)
                );

        return new ResponseEntity<>(boardCommentInfo, HttpStatus.CREATED);

    }


}
