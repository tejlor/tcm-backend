package pl.olawa.telech.tcm.repo.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;


@Getter @Setter
@NoArgsConstructor
public class FileDto extends ElementDto {

	private Integer size;
	private String mimeType;
	private String previewMimeType;

	
	public FileDto(FileEl model){
		super(model);
	}

	@Override
	public FileEl toModel() {
		throw new UnsupportedOperationException();
	}
	
	public static List<FileDto> toFileDtoList(List<FileEl> list){
		return toDtoList(FileEl.class, FileDto.class, list);
	}

}