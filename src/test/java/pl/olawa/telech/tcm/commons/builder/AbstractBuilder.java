package pl.olawa.telech.tcm.commons.builder;

import static lombok.AccessLevel.PRIVATE;

import javax.persistence.EntityManager;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;

@Setter
@Accessors(chain = true, fluent = true)
@FieldDefaults(level = PRIVATE)
public abstract class AbstractBuilder<T extends AbstractEntity> {

	Integer id;
	
	public abstract T build();
	
	public T save(EntityManager em) {
		var result = build();
		em.persist(result);
		return result;
	}
	
	protected void fill(T entity) {
		entity.setId(id);
	}
}
