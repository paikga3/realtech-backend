package realtech.db.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.G5WriteYbBoard02;

public interface G5WriteYbBoard02Repository extends PagingAndSortingRepository<G5WriteYbBoard02, Integer> {
    List<G5WriteYbBoard02> findByWrIsComment(int wrIsComment);
}
