package pl.olawa.telech.tcm.repo.logic.interfaces;

import java.util.UUID;

import pl.olawa.telech.tcm.commons.logic.interfaces.AbstractLogic;
import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;

public interface FolderLogic extends AbstractLogic<FolderEl> {

	FolderEl loadByRef(UUID ref);

	FolderEl create(UUID parentRef, FolderEl folder);

	void copy(FolderEl folder, FolderEl copy);

}
