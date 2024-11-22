package realtech.api.front.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FileItem {
    private String fileName; // 파일 이름
    private double fileSize; // 파일 용량 (KB)
    private String downloadUrl; // 다운로드 URL
    private String uid;
    private String status;
    private String type;
}
