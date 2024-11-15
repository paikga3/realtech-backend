package realtech.db.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.Notice;

public interface NoticeRepository extends PagingAndSortingRepository<Notice, Integer> {

}
