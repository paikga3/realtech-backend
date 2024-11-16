package realtech.db.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.G5WriteYbGallery01;

public interface G5WriteYbGallery01Repository extends PagingAndSortingRepository<G5WriteYbGallery01, Integer> {
    List<G5WriteYbGallery01> findAll();
    
    List<G5WriteYbGallery01> findByWrIsComment(int wrIsComment);
    
    List<G5WriteYbGallery01> findByWrParentAndWrIsComment(int wrParent, int wrIsComment);
}
