package realtech.api.front.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import realtech.api.common.exception.PostNotFoundException;
import realtech.api.common.exception.UnauthorizedException;
import realtech.api.front.model.CommentDetail;
import realtech.api.front.model.CreateCommentParams;
import realtech.api.front.model.UpdateCommentParams;
import realtech.db.entity.Comment;
import realtech.db.repository.CommentRepository;
import realtech.util.AppUtil;
import realtech.util.SecurityUtil;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    
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
            detail.setIsAdmin(comment.getIsAdmin());
            detail.setRefTable(comment.getRefTable());
            detail.setRefId(comment.getRefId());

            // 자식 댓글 조회
            List<Comment> childComments = commentRepository.findByRefTableAndRefIdAndParentCommentIdOrderByCommentIdDesc(
                    comment.getRefTable(), comment.getRefId(), comment.getCommentId());
            detail.setReplies(buildCommentHierarchy(childComments, level + 1));

            result.add(detail);
        }
        return result;
    }
    
    
    public void addComment(CreateCommentParams params, HttpServletRequest request) throws Exception {
        String authorName = StringUtils.isNotEmpty(params.getAuthorName()) ? params.getAuthorName() : "unknown";
        if ("리얼테크".equals(authorName)) {
            throw new UnauthorizedException("관리자 이름으로 댓글등록을 할 수 없습니다.");
        }
        
        
        Comment c = new Comment();
        c.setAuthorIp(AppUtil.getClientIpFromRequest(request));
        if (SecurityUtil.isAdmin()) {
            c.setAuthorName(SecurityUtil.getAuthenticatedUser().getName());
        } else {
            c.setAuthorName(authorName);
        }
        
        c.setContent(params.getContent());
        c.setIsDeleted(0);
        c.setParentCommentId(params.getParentCommentId());
        c.setRefId(params.getRefId());
        c.setRefTable(params.getRefTable());
        c.setCreatedAt(AppUtil.getCurrentDateTime());

        if (!SecurityUtil.isAdmin() && StringUtils.isNotEmpty(params.getPassword())) {
            // 솔트 생성
            byte[] salt = AppUtil.generateSalt();
            
            // 해싱
            String hashedPassword = AppUtil.hashPassword(params.getPassword(), salt);
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            c.setSalt(saltBase64);
            c.setPassword(hashedPassword);
        }
        
        c.setIsAdmin(SecurityUtil.isAdmin() ? 1 : 0);
        
        commentRepository.save(c);
    }

    public void updateComment(UpdateCommentParams params, HttpServletRequest request) throws Exception {

        Optional<Comment> commentOpt = commentRepository.findById(params.getCommentId());
        if (commentOpt.isEmpty()) {
            throw new PostNotFoundException(String.format("존재하지 않는 댓글입니다.(%d)", params.getCommentId()));
        }
        
        Comment c = commentOpt.get();
        
        if (!SecurityUtil.isAdmin()) {
            // 솔트를 디코딩
            byte[] salt = Base64.getDecoder().decode(c.getSalt());
            String hashedPassword = AppUtil.hashPassword(params.getPassword(), salt);
            if (!hashedPassword.equals(c.getPassword())) {
                throw new UnauthorizedException("댓글 비밀번호가 일치하지 않습니다.");
            }
        }
        
        c.setEditorIp(AppUtil.getClientIpFromRequest(request));
        if (SecurityUtil.isAdmin()) {
            c.setEditorName(SecurityUtil.getAuthenticatedUser().getName());
        } else {
            c.setEditorName(c.getAuthorName());
        }
        
        c.setEditedAt(AppUtil.getCurrentDateTime());
        c.setContent(params.getContent());
        
        commentRepository.save(c);
    }
    
    public void deleteComment(UpdateCommentParams params, HttpServletRequest request) throws Exception {
        Optional<Comment> commentOpt = commentRepository.findById(params.getCommentId());
        if (commentOpt.isEmpty()) {
            throw new PostNotFoundException(String.format("존재하지 않는 댓글입니다.(%d)", params.getCommentId()));
        }
        
        Comment c = commentOpt.get();
        
        if (!SecurityUtil.isAdmin()) {
            // 솔트를 디코딩
            byte[] salt = Base64.getDecoder().decode(c.getSalt());
            String hashedPassword = AppUtil.hashPassword(params.getPassword(), salt);
            if (!hashedPassword.equals(c.getPassword())) {
                throw new UnauthorizedException("댓글 비밀번호가 일치하지 않습니다.");
            }
        }
        
        
        c.setEditorIp(AppUtil.getClientIpFromRequest(request));
        if (SecurityUtil.isAdmin()) {
            c.setEditorName(SecurityUtil.getAuthenticatedUser().getName());
        } else {
            c.setEditorName(c.getAuthorName());
        }

        c.setEditedAt(AppUtil.getCurrentDateTime());
        c.setIsDeleted(1);
        
        commentRepository.save(c);
    }
    
    
}
