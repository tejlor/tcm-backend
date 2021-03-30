package pl.olawa.telech.tcm.repo.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.dto.AbstractModifiableDto;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;
import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ElementDto extends AbstractModifiableDto {

	String ref;
	String name;
	// out
	String typeName;
	List<String> featureNames;
	// in
	String parentRef; 
	
	public ElementDto(Element model) {
		super(model);
		typeName = model.getTypeName();
		featureNames = model.getFeatures().stream()
				.map(f -> f.getName())
				.sorted()
				.collect(Collectors.toList());
	}

	@Override
	public AbstractEntity toModel() {
		var model = new Element();
		fillModel(model);
		return model;
	}
	
	public static ElementDto toDto(Element model) {
		if(model instanceof FileEl) {
			return new FileDto((FileEl) model);
		}	
		if(model instanceof FolderEl) {
			return new FolderDto((FolderEl) model);
		}		
		return new ElementDto(model);
	}

	public static List<ElementDto> toDtoList(List<Element> list){
		return toDtoList(Element.class, ElementDto.class, list);
	}
}
