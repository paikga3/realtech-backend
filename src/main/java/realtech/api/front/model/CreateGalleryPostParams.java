package realtech.api.front.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateGalleryPostParams {
    private String title;
    private String content;
    private MultipartFile thumbnail;
    private List<MultipartFile> attachments;
    private String entity;
}
