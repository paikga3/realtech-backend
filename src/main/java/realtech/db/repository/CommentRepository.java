package realtech.db.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.Comment;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer> {
    List<Comment> findByRefTableAndRefId(String refTable, int refId);

    List<Comment> findByRefTableAndRefIdAndParentCommentIdOrderByCommentIdDesc(String refTable, int refId, int parentCommentId);
}
