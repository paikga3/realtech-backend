package realtech.api.front.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.common.model.PagedResponse;
import realtech.api.front.model.CreateReservationInquiryPostParams;
import realtech.api.front.model.FetchReservationInquiriesParams;
import realtech.api.front.model.ReservationInquiryPost;
import realtech.api.front.model.ReservationInquiryPostDetail;
import realtech.api.front.service.ReservationInquiryService;
import realtech.util.AesEncryptionUtil;
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
            @RequestParam(value = "edit", defaultValue = "view") String purpose) throws Exception {
        // 토큰 검증 로직 (JWT 파싱 및 검증)
        String token = AesEncryptionUtil.decode(authorizationHeader.substring(7));
        JwtValidator.validateToken(token, "reservation_inquiry", id);
        boolean isUpdateViewCount = purpose.equals("view");
        return reservationInquiryService.getReservationInquiryDetail(id, isUpdateViewCount);
    }
    
    // 게시물 등록 API
    @PostMapping("/api/reservation-inquiry-post")
    public void createReservationInquiryPost(@ModelAttribute CreateReservationInquiryPostParams params, HttpServletRequest request) throws Exception {
        reservationInquiryService.createReservationInquiryPost(params, request);
    }
    
    // 게시물 수정 API
    @PutMapping("/api/reservation-inquiry-post/{id}")
    public void updateReservationInquiryPost(@PathVariable("id") int id, @ModelAttribute CreateReservationInquiryPostParams params, HttpServletRequest request) throws Exception {
        reservationInquiryService.updateReservationInquiryPost(id, params, request);
    }
    
    // 게시물 삭제 API
    @DeleteMapping("/api/reservation-inquiry-post/{id}")
    public void deleteReservationInquiryPost(@PathVariable("id") int id) {
        reservationInquiryService.deleteReservationInquiryPost(id);
    }
    
    
    
}
