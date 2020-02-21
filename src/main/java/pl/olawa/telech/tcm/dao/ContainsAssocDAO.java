package pl.olawa.telech.tcm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.assoc.ContainsAssoc;


public interface ContainsAssocDAO extends DAO<ContainsAssoc>, JpaSpecificationExecutor<ContainsAssoc> {
	
	List<ContainsAssoc> findByParentElementId(Integer parentId);
	
}
