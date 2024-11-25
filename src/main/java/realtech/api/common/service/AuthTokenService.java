package realtech.api.common.service;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import realtech.api.common.exception.PostNotFoundException;
import realtech.db.entity.CustomerReview;
import realtech.db.entity.ReservationInquiry;
import realtech.db.repository.CustomerReviewRepository;
import realtech.db.repository.ReservationInquiryRepository;
import realtech.util.AppUtil;

@Service
public class AuthTokenService {
    
    @Autowired
    private ReservationInquiryRepository reservationInquiryRepository;
    
    @Autowired
    private CustomerReviewRepository customerReviewRepository;
    

    public boolean validatePassword(String boardType, int postId, String password) throws Exception {
        if ("reservation_inquiry".equals(boardType)) {
            Optional<ReservationInquiry> postOpt = reservationInquiryRepository.findById(postId);
            if (postOpt.isEmpty()) {
                throw new PostNotFoundException("ID가 " + postId + "인 예약문의를 찾을 수 없습니다.");
            } else {
                ReservationInquiry post = postOpt.get();
                // 솔트를 디코딩
                byte[] salt = Base64.getDecoder().decode(post.getSalt());
                String hashedPassword = AppUtil.hashPassword(password, salt);
                return hashedPassword.equals(post.getPassword());
            }
        } else if ("customer_review".equals(boardType)) {
            
            Optional<CustomerReview> postOpt = customerReviewRepository.findById(postId);
            if (postOpt.isEmpty()) {
                throw new PostNotFoundException("ID가 " + postId + "인 고객후기를 찾을 수 없습니다.");
            } else {
                CustomerReview post = postOpt.get();
                // 솔트를 디코딩
                byte[] salt = Base64.getDecoder().decode(post.getSalt());
                String hashedPassword = AppUtil.hashPassword(password, salt);
                return hashedPassword.equals(post.getPassword());
            }
        }

        return false;
    }
}
