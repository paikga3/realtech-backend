package realtech.api.front.model;

import java.util.List;

import lombok.Data;

@Data
public class NoticeDetail {
    private int id;
    private String authorIp;
    private String authorName;
    private String title;
    private String content;
    private String createdAt;
    private int views;
    private List<FileItem> attachments;
}
