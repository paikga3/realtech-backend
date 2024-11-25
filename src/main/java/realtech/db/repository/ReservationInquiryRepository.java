package realtech.db.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.ReservationInquiry;

public interface ReservationInquiryRepository extends PagingAndSortingRepository<ReservationInquiry, Integer> {
    
    List<ReservationInquiry> findAll();
    
}
