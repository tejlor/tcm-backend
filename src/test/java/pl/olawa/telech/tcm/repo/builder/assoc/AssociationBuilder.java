package pl.olawa.telech.tcm.repo.builder.assoc;

import static lombok.AccessLevel.PRIVATE;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.builder.AbstractCreatableBuilder;
import pl.olawa.telech.tcm.repo.model.entity.assoc.Association;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class AssociationBuilder<T extends Association> extends AbstractCreatableBuilder<T> {
		
	Element parentElement;						
	Element childElement;		
	
	@Override
	@SuppressWarnings("unchecked")
	public T build() {
		var obj = (T) new Association();
		fill(obj);
		return obj;	
	}
	
	@Override
	protected void fill(T assoc) {
		super.fill(assoc);
		assoc.setParentElement(parentElement);
		assoc.setChildElement(childElement);
	}
}
