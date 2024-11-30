package realtech.api.front.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NoticePost {
    private int id; // 공지사항 고유 ID
    private String title; // 제목
    private String authorName; // 작성자 이름
    private String createdAt; // 작성일
    private int commentCount; // 댓글 갯수
    private boolean hasAttachment; // 첨부파일 여부
    
    @JsonProperty("isNew")
    private boolean isNew; // 신규 글 여부
    
    
    public NoticePost(int id, String title, String authorName, String createdAt) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.createdAt = createdAt;
    }
}
