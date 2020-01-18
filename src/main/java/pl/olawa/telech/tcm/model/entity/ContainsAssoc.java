package pl.olawa.telech.tcm.model.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.Getter;
import lombok.Setter;

/*
 * Połączenie obrazujące zawieranie się pliku w folderze.
 */
@Entity
@Getter @Setter
@PrimaryKeyJoinColumn
public class ContainsAssoc extends Association {

	public ContainsAssoc() {
		
	}
	
	public ContainsAssoc(Long id) {
		super(id);
	}
}
