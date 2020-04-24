package pl.olawa.telech.tcm.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.element.Folder;


public interface FolderDAO extends DAO<Folder>, JpaSpecificationExecutor<Folder> {

	Folder findByRef(UUID ref);
	
}
