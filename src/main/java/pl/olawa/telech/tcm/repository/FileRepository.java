package pl.olawa.telech.tcm.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.File;


public interface FileRepository extends TRepository<File>, JpaSpecificationExecutor<File> {

	

}

