package ToyProject1.hungrytech.service.file;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@NoArgsConstructor
public class FileService {

    private static final String LOCAL_PATH = "C:/Temp/";

    public String fileUpload(MultipartFile file) throws Exception{

        String localPath = createLocalPath(file.getOriginalFilename());

        String boardImgPath = createBoardImgPath(localPath);

        File localDir = new File(LOCAL_PATH);

        File dest = new File(localPath);

        //해당 디렉토리가 존재하지 않으면 생성
        if(!localDir.exists()) {
            localDir.mkdir();
        }

        file.transferTo(dest);


        return boardImgPath;

    }

    private String createLocalPath(String originalFilename) {
        return LOCAL_PATH
                + UUID.randomUUID().toString()
                + originalFilename;
    }

    private String createBoardImgPath(String localPath) {
        return localPath.replaceFirst(LOCAL_PATH, "");
    }


    public boolean deleteFile(String boardImgPath) {
        //기존게시글에 이미지파일없음
        if (boardImgPath == null) {
            return false;
        }

        String localPath = changeLocalPath(boardImgPath);

        File file = new File(localPath);

        //해당 경로에 파일이 존재한다면 삭제
        if(file.exists())  {
            file.delete();
            return true;
        }

        //존재하지 않다면 삭제불가
        return false;
    }

    public String changeLocalPath(String boardImgPath) {
        return LOCAL_PATH + boardImgPath;
    }


}
