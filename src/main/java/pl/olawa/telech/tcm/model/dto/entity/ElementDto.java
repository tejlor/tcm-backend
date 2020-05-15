package pl.olawa.telech.tcm.model.dto.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.model.entity.element.Element;
import pl.olawa.telech.tcm.model.entity.element.File;
import pl.olawa.telech.tcm.model.entity.element.Folder;

@Getter @Setter
@NoArgsConstructor
public class ElementDto extends AbstractDto {

	private String ref;
	private String name;
	private LocalDateTime createdTime;
	private Integer createdById;
	private String createdByName;
	private Integer modifiedById;
	private String modifiedByName;
	
	private String typeName;
	private String parentRef; 
	
	
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
		Element model = new Element();
		fillModel(model);
		return model;
	}
	
	public static ElementDto toDto(Element model) {
		if(model instanceof File) {
			return new FileDto((File) model);
		}
		
		if(model instanceof Folder) {
			return new FolderDto((Folder) model);
		}
		
		return new ElementDto(model);
	}

	public static List<ElementDto> toDtoList(List<Element> list){
		return toDtoList(Element.class, ElementDto.class, list);
	}
}
