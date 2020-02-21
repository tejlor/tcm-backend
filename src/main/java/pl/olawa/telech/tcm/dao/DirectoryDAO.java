package pl.olawa.telech.tcm.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.element.Directory;


public interface DirectoryDAO extends DAO<Directory>, JpaSpecificationExecutor<Directory> {

	Directory findByRef(UUID ref);
	
}
