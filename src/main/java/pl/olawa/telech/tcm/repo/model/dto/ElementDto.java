package pl.olawa.telech.tcm.repo.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.dto.AbstractDto;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;
import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ElementDto extends AbstractDto {

	String ref;
	String name;
	LocalDateTime createdTime;
	Integer createdById;
	String createdByName;
	Integer modifiedById;
	String modifiedByName;
	
	String typeName;
	String parentRef; 
	
	
	public ElementDto(Element model) {
		super(model);
		typeName = model.getTypeName();
		if(model.getCreatedBy() != null) {
			createdByName = model.getCreatedBy().calcFirstLastName();
		}	
		if(model.getModifiedBy() != null) {
			modifiedByName = model.getModifiedBy().calcFirstLastName();
		}
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
