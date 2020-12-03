package pl.olawa.telech.tcm.repo.builder;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.administration.model.entity.User;
import pl.olawa.telech.tcm.commons.builder.AbstractBuilder;
import pl.olawa.telech.tcm.commons.utils.TConstants;
import pl.olawa.telech.tcm.repo.model.entity.assoc.ContainsAssoc;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

@Setter
@Accessors(chain = true, fluent = true)
@FieldDefaults(level = PRIVATE)
public class ElementBuilder extends AbstractBuilder<Element> {
	
	UUID ref = TConstants.ROOT_UUID;						
	String name = "Example element";					
	LocalDateTime createdTime = LocalDateTime.now();		
	Integer createdById = 1;			
	User createdBy;
	LocalDateTime modifiedTime = LocalDateTime.now();		
	Integer modifiedById = 1;		
	User modifiedBy;
	List<ContainsAssoc> parents = new ArrayList<>();		
	List<ContainsAssoc> children = new ArrayList<>();			
	
	@Override
	public Element build() {
		var element = new Element();
		element.setRef(ref);
		element.setName(name);
		element.setCreatedTime(createdTime);
		element.setCreatedById(createdById);
		element.setCreatedBy(createdBy);
		element.setModifiedTime(modifiedTime);
		element.setModifiedById(modifiedById);
		element.setModifiedBy(modifiedBy);
		element.setParents(parents);
		element.setChildren(children);
		return element;	
	}
}
