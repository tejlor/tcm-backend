package pl.olawa.telech.tcm.commons.builder;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.commons.model.entity.AbstractModifiableEntity;

@Setter
@Accessors(chain = true, fluent = true)
@FieldDefaults(level = PRIVATE)
public abstract class AbstractModifiableBuilder<T extends AbstractModifiableEntity> extends AbstractCreatableBuilder<T> {
	
	User modifiedBy = new User(1);				
	LocalDateTime modifiedTime = LocalDateTime.now();
	
	@Override
	public void fill(T entity) {
		super.fill(entity);
		entity.setModifiedBy(modifiedBy);
		entity.setModifiedTime(modifiedTime);
	}
}
