package pl.olawa.telech.tcm.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.assoc.ContainsAssoc;


public interface ContainsAssocRepository extends TRepository<ContainsAssoc>, JpaSpecificationExecutor<ContainsAssoc> {

	

}

