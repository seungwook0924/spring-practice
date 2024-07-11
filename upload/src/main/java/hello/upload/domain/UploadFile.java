package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {
    private String uploadFileName; //사용자가 지정한 파일명
    private String storeFileName; // 실제 서버에 저장되는 파일명

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}