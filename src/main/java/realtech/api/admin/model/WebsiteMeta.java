package realtech.api.admin.model;

import lombok.Data;

@Data
public class WebsiteMeta {
    private int id;
    private String pageTitle;
    private String ogUrl;
    private String ogTitle;
    private String ogDescription;
    private String ogImage;
    private String naverKey;
}
