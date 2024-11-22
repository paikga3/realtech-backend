package realtech.api.front.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GalleryPostDetailData {
    private int id;
    private String title;
    private String content;
    private String createdAt;
    private int views;
    private List<FileItem> attachments;
}
