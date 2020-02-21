package pl.olawa.telech.tcm.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.User;


public interface UserDAO extends DAO<User>, JpaSpecificationExecutor<User> {

	User findByEmail(String email);

}

