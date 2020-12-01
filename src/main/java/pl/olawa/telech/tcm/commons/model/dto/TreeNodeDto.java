package pl.olawa.telech.tcm.commons.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.interfaces.Loggable;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;

/*
 * 
 */
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class TreeNodeDto implements Comparable<TreeNodeDto>, Loggable {
	
	String ref;
	String name;
	boolean isLeaf;
	List<TreeNodeDto> children;
	
	public TreeNodeDto(Element element) {
		ref = element.getRef().toString();
		name = element.getName();
		isLeaf = (element instanceof FileEl);
		
		if(element.getChildrenElements() != null) {
			children = element.getChildrenElements().stream()
				.map(e -> new TreeNodeDto(e))
				.sorted()
				.collect(Collectors.toList());	
		}
	}

	@Override
	public int compareTo(TreeNodeDto other) {
		if(this.isLeaf != other.isLeaf) 				
			return this.isLeaf ? -1 : 1; // files first
								
		return this.name.compareToIgnoreCase(other.name); // then by name 
	}
}
