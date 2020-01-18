package pl.olawa.telech.tcm.model.entity.assoc;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Połączenie obrazujące zawieranie się pliku w folderze.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn
public class ContainsAssoc extends Association {

	
	public ContainsAssoc(Integer id) {
		super(id);
	}
}
