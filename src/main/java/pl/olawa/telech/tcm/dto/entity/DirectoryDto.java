package pl.olawa.telech.tcm.dto.entity;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.model.entity.element.Directory;


@Getter @Setter
@NoArgsConstructor
public class DirectoryDto extends ElementDto {

	private String icon;
	
	private String parentRef;

	
	public DirectoryDto(Directory model){
		super(model);
	}

	@Override
	public Directory toModel() {
		Directory model = new Directory();
		fillModel(model);
		return model;
	}
	
	public static List<DirectoryDto> toDirDtoList(List<Directory> list){
		return toDtoList(Directory.class, DirectoryDto.class, list);
	}

}