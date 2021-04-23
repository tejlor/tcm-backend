package pl.olawa.telech.tcm.repo.logic.interfaces;

import java.util.List;
import java.util.UUID;

import pl.olawa.telech.tcm.commons.logic.interfaces.AbstractLogic;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttributeValue;

public interface FeatureAttributeValueLogic extends AbstractLogic<FeatureAttributeValue> {

	List<FeatureAttributeValue> save(UUID elementRef, List<FeatureAttributeValue> featureValues);

}
