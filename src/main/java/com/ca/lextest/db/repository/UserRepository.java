package com.ca.lextest.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ca.lextest.db.model.User;


/*JpaRepository
	Methods inherited from interface org.springframework.data.repository.PagingAndSortingRepository
		findAll
	Methods inherited from interface org.springframework.data.repository.CrudRepository
		count, delete, deleteAll, deleteAll, deleteById, existsById, findById, save
	Methods inherited from interface org.springframework.data.repository.query.QueryByExampleExecutor
		count, exists, findAll, findOne
*/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
