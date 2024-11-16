package realtech;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.transaction.Transactional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import realtech.db.entity.CeilingTvPost;
import realtech.db.entity.Comment;
import realtech.db.entity.CustomerReview;
import realtech.db.entity.G5WriteYbBoard01;
import realtech.db.entity.G5WriteYbBoard02;
import realtech.db.entity.G5WriteYbGallery01;
import realtech.db.entity.G5WriteYbGallery02;
import realtech.db.entity.G5WriteYbReservation01;
import realtech.db.entity.Notice;
import realtech.db.entity.ReservationInquiry;
import realtech.db.entity.WallTvPost;
import realtech.db.repository.CeilingTvPostRepository;
import realtech.db.repository.CommentRepository;
import realtech.db.repository.CustomerReviewRepository;
import realtech.db.repository.G5WriteYbBoard01Repository;
import realtech.db.repository.G5WriteYbBoard02Repository;
import realtech.db.repository.G5WriteYbGallery01Repository;
import realtech.db.repository.G5WriteYbGallery02Repository;
import realtech.db.repository.G5WriteYbReservation01Repository;
import realtech.db.repository.NoticeRepository;
import realtech.db.repository.ReservationInquiryRepository;
import realtech.db.repository.WallTvPostRepository;

/**
 * 기존 데이터베이스에서 새로운 데이터베이스로 데이터를 옮기는 기능을 테스트
 */
@SpringBootTest
public class DatabaseMigrationTest {
    
    @Autowired
    private G5WriteYbGallery01Repository g5WriteYbGallery01Repository;
    
    @Autowired
    private G5WriteYbGallery02Repository g5WriteYbGallery02Repository;
    
    @Autowired
    private WallTvPostRepository wallTvPostRepository;
    
    @Autowired
    private CeilingTvPostRepository ceilingTvPostRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private G5WriteYbReservation01Repository g5WriteYbReservation01Repository;
    
    @Autowired
    private ReservationInquiryRepository reservationInquiryRepository;
    
    @Autowired
    private G5WriteYbBoard01Repository g5WriteYbBoard01Repository;
    
    @Autowired
    private G5WriteYbBoard02Repository g5WriteYbBoard02Repository;
    
    @Autowired
    private NoticeRepository noticeRepository;
    
    @Autowired
    private CustomerReviewRepository customerReviewRepository;
    
    
    
    
    public void shouldMigrateDataFromOldToNewDatabase01() {
        // 1. 기존 DB에서 데이터를 읽어오는 로직
        // 2. 새로운 DB로 데이터를 이동하는 로직
        // 3. 결과 검증: 새로운 DB에 데이터가 잘 옮겨졌는지 확인
        
        List<G5WriteYbGallery01> galleries = g5WriteYbGallery01Repository.findByWrIsComment(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        
        for (G5WriteYbGallery01 g : galleries) {
            
            WallTvPost wtp = new WallTvPost();
            wtp.setAuthorIp(g.getWrIp());
            wtp.setAuthorName(g.getWrName());
            
            
            String content = g.getWrContent();
            Document document = Jsoup.parse(content);

            // img 태그를 모두 찾기
            Elements imgTags = document.select("img");
            
            // img 태그의 src 속성을 변경
            for (int i=0; i<imgTags.size(); i++) {
                Element imgTag = imgTags.get(i);
                String originalSrc = imgTag.attr("src"); // 기존 src 값
                String newSrc = originalSrc.replace("http://www.xn--369an22at7ae6e24kqwke6h.kr", "https://realtech-board.s3.ap-northeast-2.amazonaws.com"); // 새 src 값
                imgTag.attr("src", newSrc); // src 속성 업데이트
                
                if (i == 0) {
                    wtp.setThumbnailUrl(newSrc);
                }
            }

            // 변경된 DOM을 다시 HTML 문자열로 출력
            String updatedContent = document.html();
            wtp.setContent(updatedContent);
            wtp.setCreatedAt(sdf.format(g.getWrDatetime()));
            wtp.setTitle(g.getWrSubject());
            wtp.setViews(g.getWrHit());
            
            WallTvPost savedWtp = wallTvPostRepository.save(wtp);
            
            if (g.getWrComment() > 0) {
                List<G5WriteYbGallery01> comments = g5WriteYbGallery01Repository.findByWrParentAndWrIsComment(g.getWrParent(), 1);
                
                for (G5WriteYbGallery01 comment : comments) {
                    Comment c = new Comment();
                    
                    c.setAuthorIp(comment.getWrIp());
                    c.setAuthorName(comment.getWrName());
                    c.setContent(comment.getWrContent());
                    c.setCreatedAt(sdf.format(comment.getWrDatetime()));
                    c.setRefId(savedWtp.getPostId());
                    c.setRefTable("wall_tv_post");
                    
                    commentRepository.save(c);
                }
                
            }
        }
    }
    
    
    public void shouldMigrateDataFromOldToNewDatabase02() {
        // 1. 기존 DB에서 데이터를 읽어오는 로직
        // 2. 새로운 DB로 데이터를 이동하는 로직
        // 3. 결과 검증: 새로운 DB에 데이터가 잘 옮겨졌는지 확인
        
        List<G5WriteYbGallery02> galleries = g5WriteYbGallery02Repository.findByWrIsComment(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        
        for (G5WriteYbGallery02 g : galleries) {
            
            CeilingTvPost wtp = new CeilingTvPost();
            wtp.setAuthorIp(g.getWrIp());
            wtp.setAuthorName(g.getWrName());
            
            
            String content = g.getWrContent();
            Document document = Jsoup.parse(content);

            // img 태그를 모두 찾기
            Elements imgTags = document.select("img");
            
            // img 태그의 src 속성을 변경
            for (int i=0; i<imgTags.size(); i++) {
                Element imgTag = imgTags.get(i);
                String originalSrc = imgTag.attr("src"); // 기존 src 값
                String newSrc = originalSrc.replace("http://www.xn--369an22at7ae6e24kqwke6h.kr", "https://realtech-board.s3.ap-northeast-2.amazonaws.com"); // 새 src 값
                imgTag.attr("src", newSrc); // src 속성 업데이트
                
                if (i == 0) {
                    wtp.setThumbnailUrl(newSrc);
                }
            }

            // 변경된 DOM을 다시 HTML 문자열로 출력
            String updatedContent = document.html();
            wtp.setContent(updatedContent);
            wtp.setCreatedAt(sdf.format(g.getWrDatetime()));
            wtp.setTitle(g.getWrSubject());
            wtp.setViews(g.getWrHit());
            
            CeilingTvPost savedWtp = ceilingTvPostRepository.save(wtp);
            
            if (g.getWrComment() > 0) {
                List<G5WriteYbGallery02> comments = g5WriteYbGallery02Repository.findByWrParentAndWrIsComment(g.getWrParent(), 1);
                
                for (G5WriteYbGallery02 comment : comments) {
                    Comment c = new Comment();
                    
                    c.setAuthorIp(comment.getWrIp());
                    c.setAuthorName(comment.getWrName());
                    c.setContent(comment.getWrContent());
                    c.setCreatedAt(sdf.format(comment.getWrDatetime()));
                    c.setRefId(savedWtp.getPostId());
                    c.setRefTable("ceiling_tv_post");
                    
                    commentRepository.save(c);
                }
                
            }
        }
    }
    
    @Rollback(false)
    @Transactional
    @Test
    public void shouldMigrateDataFromOldToNewDatabase03() {
        List<G5WriteYbReservation01> list = g5WriteYbReservation01Repository.findByWrIsComment(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        
        
        
        for (G5WriteYbReservation01 g : list) {
            ReservationInquiry r = new ReservationInquiry();
            r.setAddressDetail(g.getWr4());
            r.setAuthorIp(g.getWrIp());
            if ("60인치 이하".equals(g.getWr7())) {
                r.setTvSize("A");
            } else if ("65인치 이상".equals(g.getWr7())) {
                r.setTvSize("B");
            } else if ("70인치 이상".equals(g.getWr7())) {
                r.setTvSize("C");
            } else if ("기타사항".equals(g.getWr7())) {
                r.setTvSize("Z");
            }
            
            if ("대리석/아트윌/석고/타일".equals(g.getWr8())) {
                r.setWallType("A");
            } else if ("일반벽/합판/콘크리트벽".equals(g.getWr8())) {
                r.setWallType("B");
            } else if ("기타사항".equals(g.getWr8())) {
                r.setWallType("Z");
            }
            
            if ("브라켓있음".equals(g.getWr9())) {
                r.setBracketType("A");
            } else if ("고정형 브라켓".equals(g.getWr9())) {
                r.setBracketType("B");
            } else if ("상하브라켓".equals(g.getWr9())) {
                r.setBracketType("C");
            } else if ("정품 상하좌우 브라켓".equals(g.getWr9())) {
                r.setBracketType("D");
            } else if ("32인치 전용 상하좌우 브라켓".equals(g.getWr9())) {
                r.setBracketType("E");
            } else if ("기타사항".equals(g.getWr9())) {
                r.setBracketType("Z");
            }
            
            if ("예".equals(g.getWr10())) {
                r.setSettopBoxEmbed(1);
            } else if ("아니오".equals(g.getWr10())) {
                r.setSettopBoxEmbed(0);
            }
            
            r.setIsPrivate(1);
            r.setPassword("1234");
            
            r.setInstallationDate(g.getWr6().replaceAll("-", ""));
            r.setLotAddress(g.getWr3());
            r.setRoadAddress(g.getWr2());
            r.setContact(g.getWr5());
            r.setPostalCode(g.getWr1());
            r.setName(g.getWrName());
            r.setViews(g.getWrHit());
            r.setCreatedAt(sdf.format(g.getWrDatetime()));
            
            
            String content = g.getWrContent();
            Document document = Jsoup.parse(content);

            // img 태그를 모두 찾기
            Elements imgTags = document.select("img");
            
            // img 태그의 src 속성을 변경
            for (int i=0; i<imgTags.size(); i++) {
                Element imgTag = imgTags.get(i);
                String originalSrc = imgTag.attr("src"); // 기존 src 값
                String newSrc = originalSrc.replace("http://www.xn--369an22at7ae6e24kqwke6h.kr", "https://realtech-board.s3.ap-northeast-2.amazonaws.com"); // 새 src 값
                imgTag.attr("src", newSrc); // src 속성 업데이트
            }

            // 변경된 DOM을 다시 HTML 문자열로 출력
            String updatedContent = document.html();
            r.setContent(updatedContent);
            
            ReservationInquiry ref = reservationInquiryRepository.save(r);

            
            if (g.getWrComment() > 0) {
                for (G5WriteYbReservation01 re : g.getReplies()) {
                    if (re.getWrIsComment() == 1) {
                        Comment c = new Comment();
                        c.setAuthorIp(re.getWrIp());
                        c.setAuthorName(re.getWrName());
                        c.setContent(re.getWrContent());
                        c.setCreatedAt(sdf.format(re.getWrDatetime()));
                        c.setRefId(ref.getInquiryId());
                        c.setRefTable("reservation_inquiry");
                        
                        Comment sc = commentRepository.save(c);
                        
                        if (re.getReplies().size() > 0) {
                            saveComment(re, sc, ref.getInquiryId());
                        }
                    }
                    
                    
                }
            }
            
            
        }
        
    }
    
    public void saveComment(G5WriteYbReservation01 r, Comment parent, int refId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        for (G5WriteYbReservation01 re : r.getReplies()) {
            if (re.getWrIsComment() == 1) {
                Comment c = new Comment();
                c.setAuthorIp(re.getWrIp());
                c.setAuthorName(re.getWrName());
                c.setContent(re.getWrContent());
                c.setCreatedAt(sdf.format(re.getWrDatetime()));
                c.setRefId(refId);
                c.setRefTable("reservation_inquiry");
                c.setParentCommentId(parent.getCommentId());
                
                Comment sc = commentRepository.save(c);
                
                if (re.getReplies().size() > 0) {
                    saveComment(re, sc, refId);
                }
            }
            
        }
    }
    
    public void saveComment(G5WriteYbBoard02 r, Comment parent, int refId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        for (G5WriteYbBoard02 re : r.getReplies()) {
            if (re.getWrIsComment() == 1) {
                Comment c = new Comment();
                c.setAuthorIp(re.getWrIp());
                c.setAuthorName(re.getWrName());
                c.setContent(re.getWrContent());
                c.setCreatedAt(sdf.format(re.getWrDatetime()));
                c.setRefId(refId);
                c.setRefTable("reservation_inquiry");
                c.setParentCommentId(parent.getCommentId());
                
                Comment sc = commentRepository.save(c);
                
                if (re.getReplies().size() > 0) {
                    saveComment(re, sc, refId);
                }
            }
            
        }
    }
    
    

    public void shouldMigrateDataFromOldToNewDatabase04() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        List<G5WriteYbBoard01> list = g5WriteYbBoard01Repository.findByWrIsComment(0);
        for (G5WriteYbBoard01 g : list) {
            
            Notice n = new Notice();
            n.setAuthorIp(g.getWrIp());
            n.setAuthorName(g.getWrName());
            String content = g.getWrContent();
            Document document = Jsoup.parse(content);

            // img 태그를 모두 찾기
            Elements imgTags = document.select("img");
            
            // img 태그의 src 속성을 변경
            for (int i=0; i<imgTags.size(); i++) {
                Element imgTag = imgTags.get(i);
                String originalSrc = imgTag.attr("src"); // 기존 src 값
                String newSrc = originalSrc.replace("http://www.xn--369an22at7ae6e24kqwke6h.kr", "https://realtech-board.s3.ap-northeast-2.amazonaws.com"); // 새 src 값
                imgTag.attr("src", newSrc); // src 속성 업데이트
            }

            // 변경된 DOM을 다시 HTML 문자열로 출력
            String updatedContent = document.html();
            n.setContent(updatedContent);
            n.setCreatedAt(sdf.format(g.getWrDatetime()));
            n.setTitle(g.getWrSubject());
            n.setViews(g.getWrHit());
            
            noticeRepository.save(n);
            
        }
        
        
        
        
    }
    
    
    
    public void shouldMigrateDataFromOldToNewDatabase05() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        List<G5WriteYbBoard02> list = g5WriteYbBoard02Repository.findByWrIsComment(0);
        for (G5WriteYbBoard02 g : list) {
            CustomerReview t = new CustomerReview();
            t.setAuthorIp(g.getWrIp());
            t.setAuthorName(g.getWrName());
            String content = g.getWrContent();
            Document document = Jsoup.parse(content);

            // img 태그를 모두 찾기
            Elements imgTags = document.select("img");
            
            // img 태그의 src 속성을 변경
            for (int i=0; i<imgTags.size(); i++) {
                Element imgTag = imgTags.get(i);
                String originalSrc = imgTag.attr("src"); // 기존 src 값
                String newSrc = originalSrc.replace("http://www.xn--369an22at7ae6e24kqwke6h.kr", "https://realtech-board.s3.ap-northeast-2.amazonaws.com"); // 새 src 값
                imgTag.attr("src", newSrc); // src 속성 업데이트
            }

            // 변경된 DOM을 다시 HTML 문자열로 출력
            String updatedContent = document.html();
            t.setContent(updatedContent);
            t.setCreatedAt(sdf.format(g.getWrDatetime()));
            t.setTitle(g.getWrSubject());
            t.setViews(g.getWrHit());
            
            CustomerReview ref = customerReviewRepository.save(t);
            
            if (g.getWrComment() > 0) {
                for (G5WriteYbBoard02 re : g.getReplies()) {
                    if (re.getWrIsComment() == 1) {
                        Comment c = new Comment();
                        c.setAuthorIp(re.getWrIp());
                        c.setAuthorName(re.getWrName());
                        c.setContent(re.getWrContent());
                        c.setCreatedAt(sdf.format(re.getWrDatetime()));
                        c.setRefId(ref.getReviewId());
                        c.setRefTable("customer_review");
                        
                        Comment sc = commentRepository.save(c);
                        
                        if (re.getReplies().size() > 0) {
                            saveComment(re, sc, ref.getReviewId());
                        }
                    }
                    
                    
                }
            }
        }
    }
    
    
    
    
    
    
    
    
}
