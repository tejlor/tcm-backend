package pl.olawa.telech.tcm.dto.entity;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.model.entity.element.File;


@Getter @Setter
@NoArgsConstructor
public class FileDto extends ElementDto {

	private Integer size;
	private String mimeType;

	
	public FileDto(File model){
		super(model);
	}

	@Override
	public File toModel() {
		throw new UnsupportedOperationException();
	}
	
	public static List<FileDto> toFileDtoList(List<File> list){
		return toDtoList(File.class, FileDto.class, list);
	}

}