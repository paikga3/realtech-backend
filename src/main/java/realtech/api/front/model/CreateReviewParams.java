package realtech.api.front.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateReviewParams {
    private String title;
    private String content;
    private String authorName;
    private List<MultipartFile> attachments;
}
