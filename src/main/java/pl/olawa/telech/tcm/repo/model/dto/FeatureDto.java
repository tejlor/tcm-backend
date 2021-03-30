package pl.olawa.telech.tcm.repo.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.dto.AbstractModifiableDto;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FeatureDto extends AbstractModifiableDto {

	String name;
	String code;
	Set<FeatureAttributeDto> attributes;
	
	public FeatureDto(Feature model) {
		super(model);
		attributes = FeatureAttributeDto.toDtoSet(model.getAttributes());
	}

	@Override
	public Feature toModel() {
		var model = new Feature();
		fillModel(model);
		model.setAttributes(FeatureAttributeDto.toModelSet(attributes));
		return model;
	}

	public static List<FeatureDto> toDtoList(List<Feature> list){
		return toDtoList(Feature.class, FeatureDto.class, list);
	}
}
