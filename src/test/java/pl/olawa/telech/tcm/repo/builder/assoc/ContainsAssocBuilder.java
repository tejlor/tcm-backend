package pl.olawa.telech.tcm.repo.builder.assoc;

import static lombok.AccessLevel.PRIVATE;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.repo.model.entity.assoc.ContainsAssoc;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class ContainsAssocBuilder extends AssociationBuilder<ContainsAssoc> {
				
	
	@Override
	public ContainsAssoc build() {
		var obj = new ContainsAssoc();
		super.fill(obj);
		return obj;	
	}

}
