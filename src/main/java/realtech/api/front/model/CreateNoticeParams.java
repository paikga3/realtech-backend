package realtech.api.front.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateNoticeParams {
    private String title;
    private String content;
    private List<MultipartFile> attachments;
}
