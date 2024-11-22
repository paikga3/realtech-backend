package realtech.api.common.controller;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import realtech.api.common.service.AttachmentService;
import realtech.api.common.service.S3Service;
import realtech.db.entity.Attachment;
import realtech.util.AppUtils;

@RestController
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;
    
    @Autowired
    private S3Service s3Service;
    
    @GetMapping("/api/attachments/download/{attachmentId}")
    public ResponseEntity<InputStreamResource> downloadAttachment(@PathVariable int attachmentId, @RequestHeader("User-Agent") String userAgent) {
        // 한 번의 조회로 Attachment 데이터를 가져옴
        Attachment attachment = attachmentService.getAttachmentById(attachmentId);
        
        // 파일 스트림 가져오기
        InputStream fileStream = attachmentService.downloadAttachment(attachment);
        
        // 파일 이름
        String fileName = attachment.getDisplayFilename();
        String encodedFileName;

        try {
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                // IE 브라우저 처리
                encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
            } else {
                // 기타 브라우저 처리
                encodedFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
        } catch (Exception e) {
            encodedFileName = fileName; // 기본 파일 이름 사용
        }
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";
        
        // MIME 타입과 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(fileStream));
    }
    
    
    
    @PostMapping("/api/attachments/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("table") String table) {
        try {
            // 파일 S3에 업로드
            String fileUrl = s3Service.uploadFile(file, AppUtils.generateEditorPath(table));

            // URL 반환
            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
    
}
