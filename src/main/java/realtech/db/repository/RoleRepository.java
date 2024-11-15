package realtech.db.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import realtech.db.entity.Role;

public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {

}
