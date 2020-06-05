package pl.olawa.telech.tcm.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.model.entity.element.Element;
import pl.olawa.telech.tcm.model.entity.element.FileEl;
import pl.olawa.telech.tcm.model.interfaces.Loggable;

@Getter @Setter
@NoArgsConstructor
public class TreeNodeDto implements Comparable<TreeNodeDto>, Loggable {
	
	private String ref;
	private String name;
	private boolean isLeaf;
	private List<TreeNodeDto> children;
	
	
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
