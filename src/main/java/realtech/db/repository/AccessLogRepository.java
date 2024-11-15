package realtech.db.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.AccessLog;

public interface AccessLogRepository extends PagingAndSortingRepository<AccessLog, Integer> {

}
