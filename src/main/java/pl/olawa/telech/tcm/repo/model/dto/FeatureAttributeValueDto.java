package pl.olawa.telech.tcm.repo.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.dto.AbstractModifiableDto;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttributeValue;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FeatureAttributeValueDto extends AbstractModifiableDto {

	FeatureAttributeDto attribute;
	Integer elementId;
	Object value;
	
	
	public FeatureAttributeValueDto(FeatureAttributeValue model) {
		super(model);
		
		attribute = new FeatureAttributeDto(model.getFeatureAttribute());
	}

	@Override
	public FeatureAttributeValue toModel() {
		var model = new FeatureAttributeValue();
		model.setFeatureAttribute(new FeatureAttribute(attribute.getId()));
		model.setElement(new Element(elementId));
		model.setTransientValue(value); // I am not calling fillModel() because of this mapping
		return model;
	}

	public static List<FeatureAttributeValueDto> toDtoList(Collection<FeatureAttributeValue> set){
		return toDtoList(FeatureAttributeValue.class, FeatureAttributeValueDto.class, set);
	}
}
