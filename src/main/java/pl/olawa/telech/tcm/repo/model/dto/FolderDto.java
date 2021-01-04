package pl.olawa.telech.tcm.repo.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;


@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FolderDto extends ElementDto {

	String icon;

	
	public FolderDto(FolderEl model){
		super(model);
	}

	@Override
	public FolderEl toModel() {
		var model = new FolderEl();
		fillModel(model);
		return model;
	}
	
	public static List<FolderDto> toFolderDtoList(List<FolderEl> list){
		return toDtoList(FolderEl.class, FolderDto.class, list);
	}
}
