package realtech.db.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.CustomerReview;

public interface CustomerReviewRepository extends PagingAndSortingRepository<CustomerReview, Integer> {

}
