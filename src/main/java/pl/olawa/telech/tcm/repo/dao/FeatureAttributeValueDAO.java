package pl.olawa.telech.tcm.repo.dao;

import java.util.List;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;
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
        	Join<FeatureAttributeValue, FeatureAttribute> attribute = value.join(FeatureAttributeValue.PROP_FEATURE_ATTRIBUTE);
        	Join<FeatureAttribute, Feature> feature = attribute.join(FeatureAttribute.PROP_FEATURE);
            return cb.equal(feature.get(Feature.PROP_ID), featureId);
        };
	}
	
	default Specification<FeatureAttributeValue> belongsToElement(int elementId){
        return (value, cq, cb) -> {
        	Join<FeatureAttributeValue, FeatureAttribute> attribute = value.join(FeatureAttributeValue.PROP_FEATURE_ATTRIBUTE);
        	Join<FeatureAttribute, Feature> feature = attribute.join(FeatureAttribute.PROP_FEATURE);
        	Join<Feature, Element> element = feature.join(Feature.PROP_ELEMENTS);
            return cb.equal(element.get(Element.PROP_ID), elementId);
        };
	}
}

