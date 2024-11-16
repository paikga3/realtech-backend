package realtech.api.front.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import realtech.api.common.model.PagedResponse;
import realtech.api.front.model.FetchReservationInquiriesParams;
import realtech.api.front.model.ReservationInquiryPost;
import realtech.api.front.service.ReservationInquiryService;

@RestController
public class ReservationInquiryController {
    
    @Autowired
    private ReservationInquiryService reservationInquiryService;
    
    @GetMapping("/api/reservation-inquiries")
    public PagedResponse<ReservationInquiryPost> fetchReservationInquiries(FetchReservationInquiriesParams rq) {
        return reservationInquiryService.fetchReservationInquiries(rq);
    }
    
    
}
