package pl.olawa.telech.tcm.model.entity.element;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Element with type File.
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
	
	
	public File(int id) {
		super(id);
	}
	
	@Override
	public String getTypeName() {
		return "File";
	}
}
