package pl.olawa.telech.tcm.repo.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;


public interface FolderDAO extends DAO<FolderEl>, JpaSpecificationExecutor<FolderEl> {

	FolderEl findByRef(UUID ref);
}
