package pl.olawa.telech.tcm.repo.logic.interfaces;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;

import pl.olawa.telech.tcm.commons.logic.interfaces.AbstractLogic;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttributeValue;

public interface FeatureLogic extends AbstractLogic<Feature> {

	Set<Feature> loadByElement(UUID elementRef);

	List<FeatureAttributeValue> loadValuesByElementFeature(UUID elementRef, String featureCode);
	
	Feature create(Feature _feature);

	Feature update(int id, Feature _feature);

	Pair<List<Feature>, Integer> loadTable(TableParams tableParams);

	ByteArrayOutputStream exportToXlsx();

	List<FeatureAttributeValue> loadValuesByElementFeature(UUID elementRef, int featureId);
}
