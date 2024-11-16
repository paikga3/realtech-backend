package realtech.api.front.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReservationInquiryPost {
    private int id; // 문의 번호
    private String name; // 작성자 이름
    private String contact; // 연락처
    private String status; // 상태(Answered,Pending) 
    private String createdAt; // 작성일
    
    @JsonProperty("isPrivate")
    private boolean isPrivate; // 비밀글 여부
    private boolean hasAttachment; // 첨부파일 여부
    
    @JsonProperty("isNew")
    private boolean isNew; // 신규 글 여부
    private int commentCount; // 댓글 갯수
    
    public ReservationInquiryPost(int id, String name, String contact, String createdAt, boolean isPrivate) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.createdAt = createdAt;
        this.isPrivate = isPrivate;
    }
}
