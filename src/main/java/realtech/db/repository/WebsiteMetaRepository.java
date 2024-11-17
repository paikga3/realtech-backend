package realtech.db.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.WebsiteMeta;

public interface WebsiteMetaRepository extends PagingAndSortingRepository<WebsiteMeta, Integer> {
    WebsiteMeta findById(int id);
}
