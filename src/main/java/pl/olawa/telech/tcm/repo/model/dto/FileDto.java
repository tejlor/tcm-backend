package pl.olawa.telech.tcm.repo.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;


@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FileDto extends ElementDto {

	Integer size;
	String mimeType;
	String previewMimeType;

	
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
