package pl.olawa.telech.tcm.commons.model.dto;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.entity.AbstractModifiableEntity;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PROTECTED)
public abstract class AbstractModifiableDto extends AbstractCreatableDto {

	LocalDateTime modifiedTime;
	String modifiedByName;
	
	public AbstractModifiableDto(AbstractModifiableEntity model){
		super(model);
		if(model.getModifiedById() != null) {
			modifiedByName = model.getModifiedBy().calcFirstLastName();
		}
	}
}
