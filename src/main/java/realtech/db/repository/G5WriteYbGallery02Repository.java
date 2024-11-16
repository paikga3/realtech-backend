package realtech.db.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.G5WriteYbGallery02;

public interface G5WriteYbGallery02Repository extends PagingAndSortingRepository<G5WriteYbGallery02, Integer> {
    List<G5WriteYbGallery02> findByWrIsComment(int wrIsComment);
    List<G5WriteYbGallery02> findByWrParentAndWrIsComment(int wrParent, int wrIsComment);
}
