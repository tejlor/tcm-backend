package pl.olawa.telech.tcm.model.dto.entity;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.model.entity.element.FolderEl;


@Getter @Setter
@NoArgsConstructor
public class FolderDto extends ElementDto {

	private String icon;
	
	private String parentRef;

	
	public FolderDto(FolderEl model){
		super(model);
	}

	@Override
	public FolderEl toModel() {
		FolderEl model = new FolderEl();
		fillModel(model);
		return model;
	}
	
	public static List<FolderDto> toFolderDtoList(List<FolderEl> list){
		return toDtoList(FolderEl.class, FolderDto.class, list);
	}

}