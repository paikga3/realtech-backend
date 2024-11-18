package realtech.api.admin.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UpdateWebsiteMetaParams {
    private String pageTitle;
    private String ogUrl;
    private String ogTitle;
    private String ogDescription;
    private MultipartFile ogImage;
    private String naverKey;
}
