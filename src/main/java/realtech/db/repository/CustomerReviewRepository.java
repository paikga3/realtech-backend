package realtech.db.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.CustomerReview;

public interface CustomerReviewRepository extends PagingAndSortingRepository<CustomerReview, Integer> {
    List<CustomerReview> findAll();
}
