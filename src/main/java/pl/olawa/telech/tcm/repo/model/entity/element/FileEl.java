package pl.olawa.telech.tcm.repo.model.entity.element;

import static lombok.AccessLevel.PRIVATE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/*
 * Element with type File.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@PrimaryKeyJoinColumn
@Table(name = "file", schema = "repo")
public class FileEl extends Element {

	@Column(nullable = false)
	Integer size;				// file size in bytes
	
	@Column(length = 32, nullable = false) 
	String mimeType;			// mime type of the file
	
	@Column 
	Integer previewSize;		// size of the generated preview of the file
	
	@Column(length = 32) 
	String previewMimeType;		// mime type of the generated preview of the file
	
	
	public FileEl(int id) {
		super(id);
	}
	
	@Override
	public String getTypeName() {
		return "File";
	}
	
	@Override
	public FileEl clone() {
		FileEl copy = new FileEl();
		super.fillClone(copy);	
		copy.setSize(size);
		copy.setMimeType(mimeType);
		copy.setPreviewSize(previewSize);
		copy.setPreviewMimeType(previewMimeType);	
		return copy;
	}
}
