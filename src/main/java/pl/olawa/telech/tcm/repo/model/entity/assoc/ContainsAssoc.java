package pl.olawa.telech.tcm.repo.model.entity.assoc;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Association between directory and files or directories which it contains.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@DiscriminatorValue("C")
public class ContainsAssoc extends Association {

	
	public ContainsAssoc(int id) {
		super(id);
	}
	
}
