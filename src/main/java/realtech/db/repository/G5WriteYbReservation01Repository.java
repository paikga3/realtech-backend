package realtech.db.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.G5WriteYbReservation01;

public interface G5WriteYbReservation01Repository extends PagingAndSortingRepository<G5WriteYbReservation01, Integer> {
    List<G5WriteYbReservation01> findByWrIsComment(int wrIsComment);
}
