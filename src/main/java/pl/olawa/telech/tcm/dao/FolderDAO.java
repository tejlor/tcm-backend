package pl.olawa.telech.tcm.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.element.FolderEl;


public interface FolderDAO extends DAO<FolderEl>, JpaSpecificationExecutor<FolderEl> {

	FolderEl findByRef(UUID ref);
}
