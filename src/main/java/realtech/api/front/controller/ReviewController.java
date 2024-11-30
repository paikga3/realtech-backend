package realtech.api.front.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.common.model.PagedResponse;
import realtech.api.front.model.CreateReviewParams;
import realtech.api.front.model.FetchReviewsParams;
import realtech.api.front.model.ReviewDetail;
import realtech.api.front.model.ReviewPost;
import realtech.api.front.service.ReviewService;

@RestController
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    
    
    @GetMapping("/api/review-post")
    public PagedResponse<ReviewPost> fetchReviews(FetchReviewsParams rq) {
        return reviewService.fetchReviews(rq);
    }
    
    @GetMapping("/api/review-post/{id}")
    public ReviewDetail getReviewDetail(
            @PathVariable("id") int id, 
            @RequestParam(value = "edit", defaultValue = "view") String purpose) throws Exception {
        boolean isUpdateViewCount = purpose.equals("view");
        return reviewService.getReviewDetail(id, isUpdateViewCount);
    }
    
    
    // 게시물 등록 API
    @PostMapping("/api/review-post")
    public void createReview(@ModelAttribute CreateReviewParams params, HttpServletRequest request) throws Exception {
        reviewService.createReview(params, request);
    }
    
    // 게시물 수정 API
    @PutMapping("/api/review-post/{id}")
    public void updateReview(@PathVariable("id") int id, @ModelAttribute CreateReviewParams params, HttpServletRequest request) throws Exception {
        reviewService.updateReview(id, params, request);
    }
    
    // 게시물 삭제 API
    @DeleteMapping("/api/review-post/{id}")
    public void deleteReview(@PathVariable("id") int id) {
        reviewService.deleteReview(id);
    }
    
}
