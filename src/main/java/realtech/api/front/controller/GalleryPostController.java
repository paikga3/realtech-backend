package realtech.api.front.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.common.model.PagedResponse;
import realtech.api.front.model.FetchGalleryPostsParams;
import realtech.api.front.model.GalleryPost;
import realtech.api.front.service.GalleryPostService;

@RestController
public class GalleryPostController {
    
    @Autowired
    private GalleryPostService galleryPostService;
    
    @GetMapping("/api/gallery/posts")
    public PagedResponse<GalleryPost> fetchGalleryPosts(FetchGalleryPostsParams rq) {
        return galleryPostService.fetchGalleryPosts(rq);
        
    }
    
    
}
