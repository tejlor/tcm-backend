package pl.olawa.telech.tcm.adm.dao;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;


public interface UserDAO extends DAO<User>, JpaSpecificationExecutor<User> {

	User findByEmail(String email);
	
	@SuppressWarnings("unchecked")
	default Pair<List<User>, Integer> findAll(TableParams tableParams){
		return findAllWithCount(
				null,
				tableParams.getPage(),
				tableParams.getFilter() != null ? isLike(tableParams.getFilter()) : null
				);
	}
	
	// ######################### Specifications #########################################################################################################
		
	default Specification<User> isLike(String filter){
        return (element, cq, cb) -> {
        	return cb.or(
        		cb.like(cb.lower(element.get(User.PROP_FIRST_NAME)), "%" + filter + "%"), 
        		cb.like(cb.lower(element.get(User.PROP_LAST_NAME)), "%" + filter + "%"),
        		cb.like(cb.lower(element.get(User.PROP_EMAIL)), "%" + filter + "%")
        	);
        };
	}
}
