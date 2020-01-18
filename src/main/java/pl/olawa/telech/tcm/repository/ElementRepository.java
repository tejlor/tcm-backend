package pl.olawa.telech.tcm.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.element.Element;


public interface ElementRepository extends TRepository<Element>, JpaSpecificationExecutor<Element> {

	Element findByRef(UUID ref);

}

