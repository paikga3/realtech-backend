package realtech.api.front.model;

import java.util.List;

import lombok.Data;

@Data
public class CommentDetail {
    private int id;
    private String content;
    private String authorName;
    private String authorIp;
    private String createdAt;
    private int level;
    private int isDeleted;
    private int isAdmin;
    private String refTable;
    private int refId;
    private List<CommentDetail> replies; // 자식 댓글 리스트
}
