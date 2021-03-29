package ToyProject1.hungrytech.service.file;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@NoArgsConstructor
public class FileService {

    private static final String LOCALPATH =
            "C:/Users/lee taek min/Desktop/ToyProject/" +
                    "ToyProject1/out/production/resources/static/boardImgs/";

    public String fileUpload(MultipartFile file) throws Exception{

        String localPath = createLocalPath(file.getOriginalFilename());

        String boardImgPath = createBoardImgPath(localPath);

        File dest = new File(localPath);

        file.transferTo(dest);


        return boardImgPath;

    }

    private String createLocalPath(String originalFilename) {
        return LOCALPATH
                + UUID.randomUUID().toString()
                + originalFilename;
    }

    private String createBoardImgPath(String localPath) {
        return localPath.replaceFirst(LOCALPATH, "");
    }


    public boolean deleteFile(String boardImgPath) {
        //기존게시글에 이미지파일없음
        if (boardImgPath == null) {
            return false;
        }

        String localpath = changeLocalpath(boardImgPath);

        File file = new File(localpath);

        //해당 경로에 파일이 존재한다면 삭제
        if(file.exists())  {
            file.delete();
            return true;
        }

        //존재하지 않다면 삭제불가
        return false;
    }

    private String changeLocalpath(String boardImgPath) {
        return LOCALPATH + boardImgPath;
    }


}
