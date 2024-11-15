package realtech.db.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.Attachment;

public interface AttachmentRepository extends PagingAndSortingRepository<Attachment, Integer> {

}
