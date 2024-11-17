package realtech.api.front.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import realtech.api.common.model.PagedResponse;
import realtech.api.front.model.FetchReviewsParams;
import realtech.api.front.model.Review;
import realtech.db.entity.Attachment;
import realtech.db.entity.Comment;
import realtech.db.repository.AttachmentRepository;
import realtech.db.repository.CommentRepository;
import realtech.util.AppUtils;

@Service
public class ReviewService {
    
    @PersistenceContext(name = "entityManager")
    private EntityManager entityManager;
    
    @Autowired
    private AttachmentRepository attachmentRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    /**
     * 고객후기 리스트 가져오기
     * 
     * @param rq
     * @return
     */
    public PagedResponse<Review> fetchReviews(FetchReviewsParams rq) {
        StringBuffer where = new StringBuffer();
        where.append(" from CustomerReview t ");
        where.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            if ("title".equals(rq.getSearchType())) {
                where.append(" and t.title like concat('%',:searchKeyword,'%') ");
            } else if ("content".equals(rq.getSearchType())) {
                where.append(" and t.content like concat('%',:searchKeyword,'%') ");
            } else if ("tc".equals(rq.getSearchType())) {
                where.append(" and (t.title like concat('%',:searchKeyword,'%') or t.content like concat('%',:searchKeyword,'%')) ");
            }
        }
        
        
        where.append(" order by reviewId desc ");
        
        
        
        StringBuffer countSelect = new StringBuffer(" select count(t) ")
                .append(where);
        StringBuffer bodySelect = new StringBuffer(" SELECT new realtech.api.front.model.Review(")
                .append("t.reviewId, t.title, t.authorName, t.createdAt, CASE WHEN t.isPrivate = 1 THEN true ELSE false END) ")
                .append(where);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSelect.toString(), Long.class);
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            countQuery.setParameter("searchKeyword", rq.getSearchKeyword());
        }
        
        int total = countQuery.getSingleResult().intValue();

        TypedQuery<Review> listQuery = entityManager.createQuery(bodySelect.toString(), Review.class);
        if (StringUtils.isNotEmpty(rq.getSearchKeyword())) {
            listQuery.setParameter("searchKeyword", rq.getSearchKeyword());
        }
        
        int start = (rq.getPage()-1) * rq.getPageSize();
        listQuery.setFirstResult(start); // 0부터 시작
        listQuery.setMaxResults(rq.getPageSize());
        
        
        List<Review> posts = listQuery.getResultList();
        for (Review post : posts) {
            List<Comment> comments = commentRepository.findByRefTableAndRefId("customer_review", post.getId())
                    .stream()
                    .filter(a -> {
                        return a.getIsDeleted() == 0;
                    })
                    .collect(Collectors.toList());
            

            List<Attachment> attachments = attachmentRepository.findByRefTableAndRefId("customer_review", post.getId());
            
            post.setCommentCount(comments.size());
            post.setHasAttachment(!attachments.isEmpty());

            
            // 등록한지 10분 이내인 경우 신규글 마킹
            post.setNew(AppUtils.isNewPost(post.getCreatedAt()));
        }
        
        PagedResponse<Review> pr = new PagedResponse<>();
        pr.setData(posts);
        pr.setTotal(total);
        pr.setCurrentPage(rq.getPage());
        pr.setPageSize(rq.getPageSize());
        
        return pr;
    }
}
