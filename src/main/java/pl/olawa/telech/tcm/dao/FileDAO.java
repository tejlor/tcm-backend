package pl.olawa.telech.tcm.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.element.FileEl;


public interface FileDAO extends DAO<FileEl>, JpaSpecificationExecutor<FileEl> {

	FileEl findByRef(UUID ref);
	
}

