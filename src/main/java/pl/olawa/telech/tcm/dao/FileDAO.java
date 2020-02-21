package pl.olawa.telech.tcm.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.element.File;


public interface FileDAO extends DAO<File>, JpaSpecificationExecutor<File> {

	File findByRef(UUID ref);
	
}

