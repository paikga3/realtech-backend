package realtech.api.front.model;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateReservationInquiryPostParams {
    private int id;
    private String name;
    private String contact;
    private String content;
    private String postalCode;
    private String lotAddress;
    private String roadAddress;
    private String addressDetail;
    private String installationDate;
    private String tvSize;
    private String tvSizeOther;
    private String wallType;
    private String wallTypeOther;
    private String braketType;
    private String braketTypeOther;
    private int settopBoxEmbed;
    private String password;
    private List<MultipartFile> attachments;
}
