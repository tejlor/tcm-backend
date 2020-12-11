package pl.olawa.telech.tcm.commons.model.dto;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.entity.AbstractCreatableEntity;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PROTECTED)
public abstract class AbstractCreatableDto extends AbstractDto {

	LocalDateTime createdTime;
	String createdByName;
	
	public AbstractCreatableDto(AbstractCreatableEntity model){
		super(model);
	}
}
