package pl.olawa.telech.tcm.model.entity.element;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

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
@Table(name = "file", schema = "public")
public class FileEl extends Element {

	@Column
	private Integer size;				// file size in bytes
	
	@Column 
	private String mimeType;			// mime type of the file
	
	@Column 
	private Integer previewSize;			// size of the generated preview of the file
	
	@Column 
	private String previewMimeType;		// mime type of the generated preview of the file
	
	
	public FileEl(int id) {
		super(id);
	}
	
	@Override
	public String getTypeName() {
		return "File";
	}
	
	@Override
	public FileEl copy() {
		FileEl copy = new FileEl();
		super.fillCopy(copy);
		
		copy.setSize(size);
		copy.setMimeType(mimeType);
		copy.setPreviewSize(previewSize);
		copy.setPreviewMimeType(previewMimeType);
		
		return copy;
	}
}
