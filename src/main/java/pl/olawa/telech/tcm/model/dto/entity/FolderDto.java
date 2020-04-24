package pl.olawa.telech.tcm.model.dto.entity;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.model.entity.element.Folder;


@Getter @Setter
@NoArgsConstructor
public class FolderDto extends ElementDto {

	private String icon;
	
	private String parentRef;

	
	public FolderDto(Folder model){
		super(model);
	}

	@Override
	public Folder toModel() {
		Folder model = new Folder();
		fillModel(model);
		return model;
	}
	
	public static List<FolderDto> toFolderDtoList(List<Folder> list){
		return toDtoList(Folder.class, FolderDto.class, list);
	}

}