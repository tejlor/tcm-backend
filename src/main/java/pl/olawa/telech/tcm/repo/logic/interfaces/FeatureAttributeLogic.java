package pl.olawa.telech.tcm.repo.logic.interfaces;

import pl.olawa.telech.tcm.commons.logic.interfaces.AbstractLogic;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;

public interface FeatureAttributeLogic extends AbstractLogic<FeatureAttribute> {

	FeatureAttribute create(FeatureAttribute _attribute);

	FeatureAttribute update(int id, FeatureAttribute _attribute);

}
