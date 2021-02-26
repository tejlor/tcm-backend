package pl.olawa.telech.tcm.adm.dao;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.adm.model.entity.UserGroup;
import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;


public interface UserGroupDAO extends DAO<UserGroup>, JpaSpecificationExecutor<UserGroup> {
	
	@SuppressWarnings("unchecked")
	default Pair<List<UserGroup>, Integer> findAll(TableParams tableParams){
		return findAllWithCount(
				null,
				tableParams.getPage(),
				tableParams.getFilter() != null ? isLike(tableParams.getFilter()) : null
				);
	}
	
	// ######################### Specifications #########################################################################################################
		
	default Specification<UserGroup> isLike(String filter){
        return (element, cq, cb) -> {
        	return cb.like(cb.lower(element.get(UserGroup.PROP_NAME)), "%" + filter + "%");
        };
	}
}
