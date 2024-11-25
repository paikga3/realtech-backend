package realtech.api.front.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.common.model.PagedResponse;
import realtech.api.front.model.FetchReservationInquiriesParams;
import realtech.api.front.model.ReservationInquiryPost;
import realtech.api.front.model.ReservationInquiryPostDetail;
import realtech.api.front.service.ReservationInquiryService;
import realtech.util.JwtValidator;

@RestController
public class ReservationInquiryController {
    
    @Autowired
    private ReservationInquiryService reservationInquiryService;
    
    @GetMapping("/api/reservation-inquiry-post")
    public PagedResponse<ReservationInquiryPost> fetchReservationInquiries(FetchReservationInquiriesParams rq) {
        return reservationInquiryService.fetchReservationInquiries(rq);
    }
    
    // 게시글 상세 조회 API
    @GetMapping("/api/reservation-inquiry-post/{id}")
    public ReservationInquiryPostDetail getReservationInquiryPostDetail(
            @PathVariable("id") int id, 
            @RequestHeader("Authorization") String authorizationHeader, 
            @RequestParam(value = "edit", defaultValue = "view") String purpose) {
        // 토큰 검증 로직 (JWT 파싱 및 검증)
        String token = authorizationHeader.substring(7);
        JwtValidator.validateToken(token, "reservation_inquiry", id);
        boolean isUpdateViewCount = purpose.equals("view");
        return reservationInquiryService.getReservationInquiryDetail(id, isUpdateViewCount);
    }
}
