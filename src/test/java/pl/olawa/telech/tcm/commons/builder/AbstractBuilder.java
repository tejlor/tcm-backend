package pl.olawa.telech.tcm.commons.builder;

import javax.persistence.EntityManager;

import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;

public abstract class AbstractBuilder<T extends AbstractEntity> {

	public abstract T build();
	
	public T save(EntityManager em) {
		var result = build();
		em.persist(result);
		return result;
	}
}
