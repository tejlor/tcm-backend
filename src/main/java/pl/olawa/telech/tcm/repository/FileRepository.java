package pl.olawa.telech.tcm.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.element.File;


public interface FileRepository extends TRepository<File>, JpaSpecificationExecutor<File> {

	File findByRef(UUID ref);

}

