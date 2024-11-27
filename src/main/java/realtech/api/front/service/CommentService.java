package realtech.api.front.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import realtech.api.common.exception.PostNotFoundException;
import realtech.api.front.model.CommentDetail;
import realtech.api.front.model.CreateCommentParams;
import realtech.api.front.model.UpdateCommentParams;
import realtech.db.entity.Comment;
import realtech.db.entity.CustomerReview;
import realtech.db.entity.ReservationInquiry;
import realtech.db.repository.CommentRepository;
import realtech.db.repository.CustomerReviewRepository;
import realtech.db.repository.ReservationInquiryRepository;
import realtech.util.AppUtil;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private ReservationInquiryRepository reservationInquiryRepository;
    
    @Autowired
    private CustomerReviewRepository customerReviewRepository;
    
    public List<CommentDetail> getComments(String refTable, int refId) {
        List<Comment> rootComments = commentRepository.findByRefTableAndRefIdAndParentCommentIdOrderByCommentIdDesc(refTable, refId, 0);
        return buildCommentHierarchy(rootComments, 1);
    }

    private List<CommentDetail> buildCommentHierarchy(List<Comment> comments, int level) {
        List<CommentDetail> result = new ArrayList<>();
        for (Comment comment : comments) {

            CommentDetail detail = new CommentDetail();
            if (comment.getIsDeleted() == 0) {
                detail.setId(comment.getCommentId());
                detail.setContent(comment.getContent());
                detail.setAuthorName(comment.getAuthorName());
                detail.setAuthorIp(comment.getAuthorIp());
                detail.setCreatedAt(AppUtil.formatToCompactDateTime(comment.getCreatedAt()));
            }
            detail.setLevel(level);
            detail.setIsDeleted(comment.getIsDeleted());

            // 자식 댓글 조회
            List<Comment> childComments = commentRepository.findByRefTableAndRefIdAndParentCommentIdOrderByCommentIdDesc(
                    comment.getRefTable(), comment.getRefId(), comment.getCommentId());
            detail.setReplies(buildCommentHierarchy(childComments, level + 1));

            result.add(detail);
        }
        return result;
    }
    
    
    public void addComment(CreateCommentParams params, HttpServletRequest request) {
        String authorName = StringUtils.isNotEmpty(params.getAuthorName()) ? params.getAuthorName() : "unknown";
        if ("reservation_inquiry".equals(params.getRefTable())) {
            Optional<ReservationInquiry> postOpt = reservationInquiryRepository.findById(params.getRefId());
            if (postOpt.isEmpty()) {
                throw new PostNotFoundException("ID가 " + params.getRefId() + "인 예약문의 게시글을 찾을 수 없습니다.");
            }
            ReservationInquiry post = postOpt.get();
            authorName = post.getName();
        } else if ("customer_review".equals(params.getRefTable())) {
            Optional<CustomerReview> postOpt = customerReviewRepository.findById(params.getRefId());
            if (postOpt.isEmpty()) {
                throw new PostNotFoundException("ID가 " + params.getRefId() + "인 고객후기 게시글을 찾을 수 없습니다.");
            }
            CustomerReview post = postOpt.get();
            authorName = post.getAuthorName();
        }
        
        
        Comment c = new Comment();
        c.setAuthorIp(AppUtil.getClientIpFromRequest(request));
        c.setAuthorName(authorName);
        c.setContent(params.getContent());
        c.setIsDeleted(0);
        c.setParentCommentId(params.getParentCommentId());
        c.setRefId(params.getRefId());
        c.setRefTable(params.getRefTable());
        c.setCreatedAt(AppUtil.getCurrentDateTime());
        
        commentRepository.save(c);
    }

    public void updateComment(UpdateCommentParams params, HttpServletRequest request) {
        Optional<Comment> commentOpt = commentRepository.findById(params.getCommentId());
        if (commentOpt.isEmpty()) {
            throw new PostNotFoundException(String.format("존재하지 않는 댓글입니다.(%d)", params.getCommentId()));
        }
        
        Comment c = commentOpt.get();
        c.setEditorIp(AppUtil.getClientIpFromRequest(request));
        c.setEditorName(c.getAuthorName());
        c.setEditedAt(AppUtil.getCurrentDateTime());
        c.setContent(params.getContent());
        
        commentRepository.save(c);
    }
    
    public void deleteComment(UpdateCommentParams params, HttpServletRequest request) {
        Optional<Comment> commentOpt = commentRepository.findById(params.getCommentId());
        if (commentOpt.isEmpty()) {
            throw new PostNotFoundException(String.format("존재하지 않는 댓글입니다.(%d)", params.getCommentId()));
        }
        
        Comment c = commentOpt.get();
        c.setEditorIp(AppUtil.getClientIpFromRequest(request));
        c.setEditorName(c.getAuthorName());
        c.setEditedAt(AppUtil.getCurrentDateTime());
        c.setIsDeleted(1);
        
        commentRepository.save(c);
    }
    
    
}
