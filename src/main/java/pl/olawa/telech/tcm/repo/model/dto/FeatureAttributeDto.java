package pl.olawa.telech.tcm.repo.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.dto.AbstractModifiableDto;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FeatureAttributeDto extends AbstractModifiableDto {

	String name;
	String type;
	boolean required;
	
	
	public FeatureAttributeDto(FeatureAttribute model) {
		super(model);
	}

	@Override
	public FeatureAttribute toModel() {
		var model = new FeatureAttribute();	
		fillModel(model);
		return model;
	}

	public static Set<FeatureAttributeDto> toDtoSet(Set<FeatureAttribute> set){
		return toDtoSet(FeatureAttribute.class, FeatureAttributeDto.class, set);
	}
}
