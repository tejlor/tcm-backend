package pl.olawa.telech.tcm.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.element.Directory;


public interface DirectoryRepository extends TRepository<Directory>, JpaSpecificationExecutor<Directory> {

	Directory findByRef(UUID ref);

}

