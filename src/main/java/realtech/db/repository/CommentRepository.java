package realtech.db.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.Comment;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer> {

}
