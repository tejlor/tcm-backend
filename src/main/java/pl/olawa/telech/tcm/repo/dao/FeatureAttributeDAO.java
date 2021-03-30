package pl.olawa.telech.tcm.repo.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;


public interface FeatureAttributeDAO extends DAO<FeatureAttribute>, JpaSpecificationExecutor<FeatureAttribute> {
	
}

