package realtech.api.front.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.common.model.PagedResponse;
import realtech.api.front.model.CreateGalleryPostParams;
import realtech.api.front.model.FetchGalleryPostsParams;
import realtech.api.front.model.GalleryPost;
import realtech.api.front.model.GalleryPostDetailData;
import realtech.api.front.model.UpdateGalleryPostParams;
import realtech.api.front.service.GalleryPostService;

@RestController
public class GalleryPostController {
    
    @Autowired
    private GalleryPostService galleryPostService;
    
    @GetMapping("/api/gallery-post")
    public PagedResponse<GalleryPost> fetchGalleryPost(FetchGalleryPostsParams rq) {
        return galleryPostService.fetchGalleryPost(rq);
        
    }
    
    // 게시글 상세 조회 API
    @GetMapping("/api/gallery-post/{id}")
    public GalleryPostDetailData getGalleryPost(
            @PathVariable("id") int id, 
            @RequestParam("entity") String entity,
            @RequestParam(value = "edit", defaultValue = "view") String purpose) {
        boolean isUpdateViewCount = purpose.equals("view");
        return galleryPostService.getPostDetailByIdAndEntity(id, entity, isUpdateViewCount);
    }
    
    // 게시물 등록 API
    @PostMapping("/api/gallery-post")
    public void createGalleryPost(@ModelAttribute CreateGalleryPostParams params, HttpServletRequest request) {
        galleryPostService.createGalleryPost(params, request);
    }
    
    // 게시물 수정 API
    @PutMapping("/api/gallery-post")
    public void updateGalleryPost(@ModelAttribute UpdateGalleryPostParams params, HttpServletRequest request) {
        galleryPostService.updateGalleryPost(params, request);
    }
    
}
