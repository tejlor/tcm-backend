package pl.olawa.telech.tcm.repo.builder.element;

import static lombok.AccessLevel.PRIVATE;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.builder.AbstractBuilder;
import pl.olawa.telech.tcm.repo.model.entity.assoc.ContainsAssoc;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class ElementBuilder extends AbstractBuilder<Element> {
	
	UUID ref = UUID.randomUUID();						
	String name = "Example element";					
	Set<ContainsAssoc> parentsAssoc = new HashSet<>();		
	Set<ContainsAssoc> childrenAssoc = new HashSet<>();			
	
	@Override
	public Element build() {
		var obj = new Element();
		fill(obj);
		return obj;	
	}
	
	@Override
	protected void fill(Element element) {
		super.fill(element);
		element.setRef(ref);
		element.setName(name);
		element.setParentsAssoc(parentsAssoc);
		element.setChildrenAssoc(childrenAssoc);
	}
}
