package pl.olawa.telech.tcm.repo.dao;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.repo.model.entity.AccessRight;


public interface AccessRightDAO extends DAO<AccessRight>, JpaSpecificationExecutor<AccessRight> {
	
	@Modifying
    @Query(value = "DELETE FROM AccessRight WHERE elementId = :elementId")
    void deleteByElement(@Param("elementId") int elementId);

	default List<AccessRight> findByElement(int elementId){
		return findAll(		
				isForElement(elementId)
		);
	}
	
	// ######################### Specifications #########################################################################################################

	default Specification<AccessRight> isForElement(int elementRef){
        return (accessRight, cq, cb) -> {
            return cb.equal(accessRight.get(AccessRight.PROP_ELEMENT_ID), elementRef);
        };
	}
}
