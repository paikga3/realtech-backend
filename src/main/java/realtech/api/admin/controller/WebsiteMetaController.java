package realtech.api.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
}
