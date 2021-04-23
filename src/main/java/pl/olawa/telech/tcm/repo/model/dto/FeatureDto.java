package pl.olawa.telech.tcm.repo.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.Collection;
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

	public static final int MAPPING_SIMPLE = 2;
	
	String name;
	String code;
	Set<FeatureAttributeDto> attributes;
	
	public FeatureDto(Feature model) {
		this(model, MAPPING_FULL);
	}
	
	public FeatureDto(Feature model, int mode) {
		super(model);
		if((mode & MAPPING_FULL) != 0) {
			attributes = FeatureAttributeDto.toDtoSet(model.getAttributes());
		}
	}

	@Override
	public Feature toModel() {
		var model = new Feature();
		fillModel(model);
		model.setAttributes(FeatureAttributeDto.toModelSet(attributes));
		return model;
	}

	public static List<FeatureDto> toDtoList(Collection<Feature> list){
		return toDtoList(Feature.class, FeatureDto.class, list);
	}
	
	public static List<FeatureDto> toDtoList(Collection<Feature> list, int mode){
		return toDtoList(Feature.class, FeatureDto.class, list, mode);
	}
}
