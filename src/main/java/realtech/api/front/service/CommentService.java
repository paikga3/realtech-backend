package realtech.api.front.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import realtech.api.front.model.CommentDetail;
import realtech.db.entity.Comment;
import realtech.db.repository.CommentRepository;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    
    public List<CommentDetail> getComments(String refTable, int refId) {
        List<Comment> rootComments = commentRepository.findByRefTableAndRefIdAndParentCommentIdIsNull(refTable, refId);
        return buildCommentHierarchy(rootComments, 1);
    }

    private List<CommentDetail> buildCommentHierarchy(List<Comment> comments, int level) {
        List<CommentDetail> result = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getIsDeleted() == 1) continue; // 삭제된 댓글은 제외
            CommentDetail detail = new CommentDetail();
            detail.setId(comment.getCommentId());
            detail.setContent(comment.getContent());
            detail.setAuthorName(comment.getAuthorName());
            detail.setCreatedAt(comment.getCreatedAt());
            detail.setLevel(level);

            // 자식 댓글 조회
            List<Comment> childComments = commentRepository.findByRefTableAndRefIdAndParentCommentId(
                    comment.getRefTable(), comment.getRefId(), comment.getCommentId());
            detail.setReplies(buildCommentHierarchy(childComments, level + 1));

            result.add(detail);
        }
        return result;
    }
}
