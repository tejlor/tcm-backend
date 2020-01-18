package pl.olawa.telech.tcm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.Getter;
import lombok.Setter;

/*
 * Element typu folder.
 */
@Entity
@Getter @Setter
@PrimaryKeyJoinColumn
public class Directory extends Element {

	@Column
	private String icon;
	
	public Directory() {
		
	}
	
	public Directory(Long id) {
		super(id);
	}
}
