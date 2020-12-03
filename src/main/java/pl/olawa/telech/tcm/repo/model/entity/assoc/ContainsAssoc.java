package pl.olawa.telech.tcm.repo.model.entity.assoc;

import static lombok.AccessLevel.PRIVATE;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/*
 * Association between directory and files or directories which it contains.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@DiscriminatorValue("C")
public class ContainsAssoc extends Association {

	public ContainsAssoc(int id) {
		super(id);
	}	
}
