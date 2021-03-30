package pl.olawa.telech.tcm.repo.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;


public interface FeatureDAO extends DAO<Feature>, JpaSpecificationExecutor<Feature> {
	
}

