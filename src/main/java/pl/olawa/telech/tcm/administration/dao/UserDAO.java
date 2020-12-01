package pl.olawa.telech.tcm.administration.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.administration.model.entity.User;
import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;


public interface UserDAO extends DAO<User>, JpaSpecificationExecutor<User> {

	User findByEmail(String email);
}
