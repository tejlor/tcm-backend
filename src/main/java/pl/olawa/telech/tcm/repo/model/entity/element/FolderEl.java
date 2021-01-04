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
 * Element with type Folder.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@PrimaryKeyJoinColumn
@Table(name = "folder", schema = "repo")
public class FolderEl extends Element {

	@Column(length = 32)
	String icon;
	
	
	public FolderEl(int id) {
		super(id);
	}
	
	public String getTypeName() {
		return "Folder";
	}
	
	@Override
	public FolderEl copy() {
		FolderEl copy = new FolderEl();
		super.fillCopy(copy);	
		copy.setIcon(icon);	
		return copy;
	}
}
