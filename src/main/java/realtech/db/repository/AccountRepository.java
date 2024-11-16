package realtech.db.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.Account;

public interface AccountRepository extends PagingAndSortingRepository<Account, Integer> {

}
