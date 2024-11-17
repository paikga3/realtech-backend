package realtech.api.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import realtech.api.admin.model.WebsiteMeta;
import realtech.db.repository.WebsiteMetaRepository;

@Service
public class WebsiteMetaService {
    
    @Autowired
    private WebsiteMetaRepository websiteMetaRepository;
    
    
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
    
}
