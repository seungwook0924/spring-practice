package hello.upload.domain;

import lombok.Data;

import java.util.List;

@Data
public class Item {
    private Long id;
    private String itemName;
    private UploadFile attachFile; //단일 파일
    private List<UploadFile> imageFiles; //여러개의 파일
}