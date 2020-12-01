package pl.olawa.telech.tcm.repo.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;


public interface FileDAO extends DAO<FileEl>, JpaSpecificationExecutor<FileEl> {
	
	FileEl findByRef(UUID ref);
}

