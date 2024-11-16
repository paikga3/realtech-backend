package realtech.db.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.G5WriteYbBoard01;

public interface G5WriteYbBoard01Repository extends PagingAndSortingRepository<G5WriteYbBoard01, Integer> {
    List<G5WriteYbBoard01> findByWrIsComment(int wrIsComment);
}
