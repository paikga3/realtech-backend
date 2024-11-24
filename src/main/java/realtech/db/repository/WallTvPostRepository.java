package realtech.db.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.WallTvPost;

public interface WallTvPostRepository extends PagingAndSortingRepository<WallTvPost, Integer> {
    // 메서드 이름으로 정렬과 페이징 쿼리를 생성
    Page<WallTvPost> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
