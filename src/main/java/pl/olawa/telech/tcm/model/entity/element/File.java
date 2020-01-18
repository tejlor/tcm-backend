package pl.olawa.telech.tcm.model.entity.element;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.logic.AccountLogic;

/*
 * Element typu plik.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn
public class File extends Element {

	@Column
	private Integer size;			// rozmiar pliku w bajtach
	
	@Column 
	private String mimeType;		// typ pliku
	
	
	public File(Integer id) {
		super(id);
	}
	
	public static File create() {
		File file = new File();
		file.setRef(UUID.randomUUID());
		file.setCreatedBy(AccountLogic.getCurrentUser());
		file.setCreatedTime(LocalDateTime.now());
		return file;
	}
	
	@Override
	public String getTypeName() {
		return "File";
	}
}
