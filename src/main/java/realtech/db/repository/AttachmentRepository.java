package realtech.db.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.Attachment;

public interface AttachmentRepository extends PagingAndSortingRepository<Attachment, Integer> {
    
    
    List<Attachment> findByRefTableAndRefId(String refTable, int refId);
    
}
