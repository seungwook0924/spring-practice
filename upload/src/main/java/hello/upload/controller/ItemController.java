package hello.upload.controller;

import hello.upload.domain.UploadFile;
import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    //@GetMapping("/items/new") : 등록 폼을 보여준다.
    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form) {
        return "item-form";
    }

    //@PostMapping("/items/new") : 폼의 데이터를 저장하고 보여주는 화면으로 리다이렉트 한다.
    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile()); //첨부파일
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles()); //여러개의 이미지 파일

        //데이터베이스에 저장
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", item.getId());
        return "redirect:/items/{itemId}";
    }

    //@GetMapping("/items/{id}") : 상품을 보여준다.
    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view";
    }

    //@GetMapping("/images/{filename}") : <img> 태그로 이미지를 조회할 때 사용한다.
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename)); //요청받은 파일 이름을 기반으로 실제 파일의 경로를 구성 -> 해당 파일의 내용을 클라이언트에게 전송
    }

    //첨부 파일을 다운로드 할 때 실행
    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        Item item = itemRepository.findById(itemId); //아이템의 ID를 통해서 해당 객체를 가져옴
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName)); //요청받은 파일 이름을 기반으로 실제 파일의 경로를 구성하여 객체로 저장
        log.info("uploadFileName={}", uploadFileName);

        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8); //파일 이름의 인코딩 설정

        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\""; //파일 다운로드를 위한 헤더 설정
        return ResponseEntity.ok() //상태코드 200
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition) //(헤더 이름, 헤더 값) -> (응답 본문을 어떻게 나타낼지, 파일을 다운로드 하라고 지시)
                .body(resource); //이 객체를 응답 본문으로 설정함으로써, 클라이언트는 이 데이터를 파일로 다운로드할 수 있다.
    }
}