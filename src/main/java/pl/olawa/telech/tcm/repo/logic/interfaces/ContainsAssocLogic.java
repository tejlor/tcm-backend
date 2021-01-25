package pl.olawa.telech.tcm.repo.logic.interfaces;

import java.util.UUID;

import pl.olawa.telech.tcm.repo.model.entity.element.Element;

public interface ContainsAssocLogic {

	void create(UUID parentRef, Element child);

	void create(Element parent, Element child);

	void deleteParentAssoc(Element element);

}
