package pl.olawa.telech.tcm.repo.builder.assoc;

import static lombok.AccessLevel.PRIVATE;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.builder.AbstractBuilder;
import pl.olawa.telech.tcm.repo.model.entity.assoc.Association;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class AssociationBuilder extends AbstractBuilder<Association> {
		
	Element parentElement;						
	Element childElement;		
	
	@Override
	public Association build() {
		var obj = new Association();
		fill(obj);
		return obj;	
	}
	
	@Override
	protected void fill(Association assoc) {
		super.fill(assoc);
		assoc.setParentElement(parentElement);
		assoc.setChildElement(childElement);
	}
}
