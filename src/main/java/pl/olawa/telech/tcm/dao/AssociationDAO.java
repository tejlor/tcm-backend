package pl.olawa.telech.tcm.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.assoc.Association;


public interface AssociationDAO extends DAO<Association>, JpaSpecificationExecutor<Association> {
	

	
}
