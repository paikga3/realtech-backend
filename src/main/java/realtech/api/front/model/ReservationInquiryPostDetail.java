package realtech.api.front.model;

import java.util.List;

import lombok.Data;

@Data
public class ReservationInquiryPostDetail {
    private int id;
    private String addressDetail;
    private String authorIp;
    private String braketType;
    private String braketTypeOther;
    private String contact;
    private String content;
    private String createdAt;
    private String installationDate;
    private int isPrivate;
    private String lotAddress;
    private String name;
    private String postalCode;
    private String roadAddress;
    private int settopBoxEmbed;
    private String tvSize;
    private String tvSizeOther;
    private int views;
    private String wallType;
    private String wallTypeOther;
    private List<FileItem> attachments;
}
