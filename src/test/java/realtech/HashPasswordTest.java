package realtech;

import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import realtech.db.entity.CustomerReview;
import realtech.db.entity.ReservationInquiry;
import realtech.db.repository.CustomerReviewRepository;
import realtech.db.repository.ReservationInquiryRepository;
import realtech.util.AppUtil;

@SpringBootTest
public class HashPasswordTest {
    @Autowired
    private ReservationInquiryRepository reservationInquiryRepository;
    
    @Autowired
    private CustomerReviewRepository customerReviewRepository;
    
    
//    @Test
    public void test() throws Exception {
        
        List<ReservationInquiry> list = reservationInquiryRepository.findAll();
        for (ReservationInquiry post : list) {
            if (post.getIsPrivate() == 1) {
                
                String password = post.getPassword();
                
                // 솔트 생성
                byte[] salt = AppUtil.generateSalt();

                // 해싱
                String hashedPassword = AppUtil.hashPassword(password, salt);
                String saltBase64 = Base64.getEncoder().encodeToString(salt);
                post.setSalt(saltBase64);
                post.setPassword(hashedPassword);
                
                reservationInquiryRepository.save(post);
            }
        }
    }
    
    
    @Test
    public void test2() throws Exception {
        List<CustomerReview> list = customerReviewRepository.findAll();
        for (CustomerReview post : list) {
            // 솔트를 디코딩
            byte[] salt = Base64.getDecoder().decode(post.getSalt());

            // 해싱
            String hashedInputPassword = AppUtil.hashPassword("1234", salt);

            // 검증
            if (post.getPassword().equals(hashedInputPassword)) {
                System.out.println("Password matches!");
            } else {
                System.out.println("Invalid password.");
            }
            

        }
    }
}
