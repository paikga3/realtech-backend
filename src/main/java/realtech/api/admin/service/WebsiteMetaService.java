package realtech.api.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j2;
import realtech.api.admin.model.UpdateWebsiteMetaParams;
import realtech.api.admin.model.WebsiteMeta;
import realtech.api.common.service.S3UploadService;
import realtech.db.repository.WebsiteMetaRepository;

@Log4j2
@Service
public class WebsiteMetaService {
    
    @Autowired
    private WebsiteMetaRepository websiteMetaRepository;
    
    @Autowired
    private S3UploadService s3UploadService;
    
    public WebsiteMeta fetchWebsiteMeta() {
        
        realtech.db.entity.WebsiteMeta db = websiteMetaRepository.findById(1);
        WebsiteMeta m = new WebsiteMeta();
        m.setId(db.getId());
        m.setPageTitle(db.getPageTitle());
        m.setOgUrl(db.getOgUrl());
        m.setOgTitle(db.getOgTitle());
        m.setOgDescription(db.getOgDescription());
        m.setOgImage(db.getOgImage());
        m.setNaverKey(db.getNaverKey());
        
        return m;
    }
    
    public void updateWebsiteMeta(UpdateWebsiteMetaParams params) throws Exception {
        // 파라미터 값 출력 (디버깅 용)
        log.info("Page Title: " + params.getPageTitle());
        log.info("OG URL: " + params.getOgUrl());
        log.info("OG Title: " + params.getOgTitle());
        log.info("OG Description: " + params.getOgDescription());
        log.info("Naver Key: " + params.getNaverKey());
        realtech.db.entity.WebsiteMeta meta = websiteMetaRepository.findById(1);
        meta.setPageTitle(params.getPageTitle());
        meta.setOgUrl(params.getOgUrl());
        meta.setOgTitle(params.getOgTitle());
        meta.setOgDescription(params.getOgDescription());
        meta.setNaverKey(params.getNaverKey());
        
        MultipartFile ogImage = params.getOgImage();
        if (ogImage != null && !ogImage.isEmpty()) {
            // S3에 업로드
            String ogImageUrl = s3UploadService.uploadFile(ogImage, "data/setting", "realtech-board");
            meta.setOgImage(ogImageUrl);
        }
        
        websiteMetaRepository.save(meta);
    }
}
