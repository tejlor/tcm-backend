package pl.olawa.telech.tcm.commons.builder;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.commons.model.entity.AbstractCreatableEntity;

@Setter
@Accessors(chain = true, fluent = true)
@FieldDefaults(level = PRIVATE)
public abstract class AbstractCreatableBuilder<T extends AbstractCreatableEntity> extends AbstractBuilder<T> {
	
	User createdBy = new User(1);				
	LocalDateTime createdTime = LocalDateTime.now();
	
	@Override
	public void fill(T entity) {
		super.fill(entity);
		entity.setCreatedBy(createdBy);
		entity.setCreatedTime(createdTime);
	}
}
