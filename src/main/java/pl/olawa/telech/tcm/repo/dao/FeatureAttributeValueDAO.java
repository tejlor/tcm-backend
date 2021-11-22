package pl.olawa.telech.tcm.repo.dao;

import java.util.List;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute.Fields;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttributeValue;


public interface FeatureAttributeValueDAO extends DAO<FeatureAttributeValue>, JpaSpecificationExecutor<FeatureAttributeValue> {
	
	@SuppressWarnings("unchecked")
	default List<FeatureAttributeValue> loadByElementAndFeature(int elementId, int featureId) {
		return findAll(
				belongsToFeature(featureId),
				belongsToElement(elementId)
				);
	}
	
	// ######################### Specifications #########################################################################################################

	default Specification<FeatureAttributeValue> belongsToFeature(int featureId){
        return (value, cq, cb) -> {
        	Join<FeatureAttributeValue, FeatureAttribute> attribute = value.join(FeatureAttributeValue.Fields.featureAttribute);
        	Join<FeatureAttribute, Feature> feature = attribute.join(Fields.feature);
            return cb.equal(feature.get(AbstractEntity.Fields.id), featureId);
        };
	}
	
	default Specification<FeatureAttributeValue> belongsToElement(int elementId){
        return (value, cq, cb) -> {
        	Join<FeatureAttributeValue, FeatureAttribute> attribute = value.join(FeatureAttributeValue.Fields.featureAttribute);
        	Join<FeatureAttribute, Feature> feature = attribute.join(Fields.feature);
        	Join<Feature, Element> element = feature.join(Feature.Fields.elements);
            return cb.equal(element.get(AbstractEntity.Fields.id), elementId);
        };
	}
}

