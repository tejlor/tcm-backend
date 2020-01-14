package pl.olawa.telech.tcm.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.User;


public interface UserRepository extends TRepository<User>, JpaSpecificationExecutor<User> {

	User findByEmail(String email);

}

