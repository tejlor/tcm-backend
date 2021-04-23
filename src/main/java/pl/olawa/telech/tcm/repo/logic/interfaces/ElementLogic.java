package pl.olawa.telech.tcm.repo.logic.interfaces;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;

import pl.olawa.telech.tcm.commons.logic.interfaces.AbstractLogic;
import pl.olawa.telech.tcm.commons.model.shared.Path;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

public interface ElementLogic extends AbstractLogic<Element> {

	Element loadByRef(UUID ref);

	List<Element> loadChildren(UUID ref);

	Element loadParentsTree(UUID ref);

	Pair<List<Element>, Integer> loadChildren(UUID ref, TableParams tableParams);

	Path loadAbsolutePath(UUID ref);

	void rename(UUID ref, String newName);

	void move(List<UUID> refs, UUID newParentRef);

	void copy(List<UUID> refs, UUID newParentRef);

	void remove(List<UUID> refs);

	void fillNew(Element element);

	Element addFeature(UUID ref, int featureId);

}
