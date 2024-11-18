package realtech.api.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.admin.model.UpdateWebsiteMetaParams;
import realtech.api.admin.model.WebsiteMeta;
import realtech.api.admin.service.WebsiteMetaService;

@RestController
public class WebsiteMetaController {
    
    @Autowired
    private WebsiteMetaService websiteMetaService;
    
    
    @GetMapping("/api/admin/website-meta")
    public WebsiteMeta fetchWebsiteMeta() {
        return websiteMetaService.fetchWebsiteMeta();
    }
    
    
    @PutMapping("/api/admin/website-meta")
    public ResponseEntity<String> updateWebsiteMeta(@ModelAttribute UpdateWebsiteMetaParams params) throws Exception { // @ModelAttribute로 Multipart 지원
        websiteMetaService.updateWebsiteMeta(params);
        return ResponseEntity.ok("Website meta updated successfully!");
    }
}
