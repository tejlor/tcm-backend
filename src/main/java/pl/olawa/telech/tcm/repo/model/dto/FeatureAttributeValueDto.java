package pl.olawa.telech.tcm.repo.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.Collection;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.dto.AbstractModifiableDto;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttributeValue;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FeatureAttributeValueDto extends AbstractModifiableDto {

	String attributeId;
	Integer elementId;
	Object value;
	
	
	public FeatureAttributeValueDto(FeatureAttributeValue model) {
		super(model);
	}

	@Override
	public FeatureAttributeValue toModel() {
		var model = new FeatureAttributeValue();
		fillModel(model);
		return model;
	}

	public static Set<FeatureAttributeValueDto> toDtoSet(Collection<FeatureAttributeValue> set){
		return toDtoSet(FeatureAttributeValue.class, FeatureAttributeValueDto.class, set);
	}
}
