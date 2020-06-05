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
public class FileEl extends Element {

	@Column
	private Integer size;				// file size in bytes
	
	@Column 
	private String mimeType;			// mime type of the file
	
	@Column 
	private String previewSize;			// size of the generated preview of the file
	
	@Column 
	private String previewMimeType;		// mime type of the generated preview of the file
	
	
	public FileEl(int id) {
		super(id);
	}
	
	@Override
	public String getTypeName() {
		return "File";
	}
}
