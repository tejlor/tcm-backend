package pl.olawa.telech.tcm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.Getter;
import lombok.Setter;

/*
 * Element typu plik.
 */
@Entity
@Getter @Setter
@PrimaryKeyJoinColumn
public class File extends Element {

	@Column
	private Long size;				// rozmiar pliku w bajtach
	
	@Column 
	private String mimeType;		// typ pliku
	
	public File() {
		
	}
	
	public File(Long id) {
		super(id);
	}
}
