package ToyProject1.hungrytech.restController;

import ToyProject1.hungrytech.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

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


}
