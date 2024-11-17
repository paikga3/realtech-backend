package realtech.api.front.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.common.model.PagedResponse;
import realtech.api.front.model.FetchReviewsParams;
import realtech.api.front.model.Review;
import realtech.api.front.service.ReviewService;

@RestController
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    
    
    @GetMapping("/api/reviews")
    public PagedResponse<Review> fetchReviews(FetchReviewsParams rq) {
        return reviewService.fetchReviews(rq);
    }
}
