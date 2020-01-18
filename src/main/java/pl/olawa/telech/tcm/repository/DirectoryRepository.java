package pl.olawa.telech.tcm.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.Directory;
import pl.olawa.telech.tcm.model.entity.Element;


public interface DirectoryRepository extends TRepository<Directory>, JpaSpecificationExecutor<Directory> {

	Directory findByRef(UUID ref);

}

