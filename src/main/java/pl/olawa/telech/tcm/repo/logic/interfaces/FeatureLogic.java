package pl.olawa.telech.tcm.repo.logic.interfaces;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttributeValue;

public interface FeatureLogic {

	Set<Feature> loadByElement(UUID elementRef);

	List<FeatureAttributeValue> loadValuesByElementFeature(UUID elementRef, String featureCode);
	
	Feature create(Feature _feature);

	Feature update(int id, Feature _feature);

}
