package pl.olawa.telech.tcm.repo.model.entity.element;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Element with type Folder.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn
@Table(name = "folder", schema = "public")
public class FolderEl extends Element {

	@Column
	private String icon;
	
	
	public FolderEl(int id) {
		super(id);
	}
	
	public String getTypeName() {
		return "Folder";
	}
}