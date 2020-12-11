package pl.olawa.telech.tcm.repo.model.entity.element;

import static lombok.AccessLevel.PRIVATE;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.entity.AbstractModifiableEntity;
import pl.olawa.telech.tcm.repo.model.entity.assoc.ContainsAssoc;

/*
 * Element of the repository. Base class for all elements.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "element", schema = "repo")
public class Element extends AbstractModifiableEntity {

	@Column(nullable = false)
	UUID ref;							// element unique reference
	
	@Column(length = 255, nullable = false)
	String name;						// element name
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "childElementId")
	Set<ContainsAssoc> parentsAssoc;		// parents of the element
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentElementId")
	Set<ContainsAssoc> childrenAssoc;		// children of the element
	
	
	@Transient
	UUID parentRef;
	
	@Transient
	List<Element> childrenElements;
 	
	
	public Element(int id) {
		super(id);
		parentsAssoc = new HashSet<>();
		childrenAssoc = new HashSet<>();
	}
	
	public String getTypeName() {
		return "Element";
	}
	
	@Override
	public Element clone() {
		throw new UnsupportedOperationException();
	}
	
	protected void fillClone(Element clone) {
		clone.setName(name);
	}
}
