package pl.olawa.telech.tcm.model.entity.element;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Element typu folder.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn
public class Folder extends Element {

	@Column
	private String icon;
	
	
	public Folder(int id) {
		super(id);
	}
	
	public String getTypeName() {
		return "Folder";
	}
}
