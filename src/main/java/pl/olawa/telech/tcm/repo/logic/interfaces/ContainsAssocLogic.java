package pl.olawa.telech.tcm.repo.logic.interfaces;

import java.util.UUID;

import pl.olawa.telech.tcm.commons.logic.interfaces.AbstractLogic;
import pl.olawa.telech.tcm.repo.model.entity.assoc.ContainsAssoc;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

public interface ContainsAssocLogic extends AbstractLogic<ContainsAssoc> {

	void create(UUID parentRef, Element child);

	void create(Element parent, Element child);

	void deleteParentAssoc(Element element);

}
