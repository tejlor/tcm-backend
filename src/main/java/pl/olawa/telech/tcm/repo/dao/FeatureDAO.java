package pl.olawa.telech.tcm.repo.dao;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;


public interface FeatureDAO extends DAO<Feature>, JpaSpecificationExecutor<Feature> {
	
	@SuppressWarnings("unchecked")
	default Pair<List<Feature>, Integer> findAll(TableParams tableParams){
		return findAllWithCount(
				null,
				tableParams.getPage(),
				tableParams.getFilter() != null ? isLike(tableParams.getFilter()) : null
				);
	}
	
	default Feature findByCode(String code) {
		return findOne(
				hasCode(code)
			).orElse(null);
	}
	
	// ######################### Specifications #########################################################################################################
	
	default Specification<Feature> hasCode(String code){
        return (feature, cq, cb) -> {
        	return cb.equal(feature.get(Feature.PROP_CODE), code);
        };
	}
	
	default Specification<Feature> isLike(String filter){
        return (element, cq, cb) -> {
        	return cb.or(
        		cb.like(cb.lower(element.get(Feature.PROP_NAME)), "%" + filter + "%"), 
        		cb.like(cb.lower(element.get(Feature.PROP_CODE)), "%" + filter + "%")
        	);
        };
	}

}

