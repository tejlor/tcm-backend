package pl.olawa.telech.tcm.repo.logic.interfaces;

import java.util.UUID;

import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;

public interface FolderLogic {

	FolderEl loadByRef(UUID ref);

	FolderEl create(UUID parentRef, FolderEl folder);

	void copy(FolderEl folder, FolderEl copy);

}
