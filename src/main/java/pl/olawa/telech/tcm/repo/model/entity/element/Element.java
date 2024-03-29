package pl.olawa.telech.tcm.repo.model.entity.element;

import static lombok.AccessLevel.PRIVATE;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import pl.olawa.telech.tcm.commons.model.entity.AbstractModifiableEntity;
import pl.olawa.telech.tcm.repo.model.entity.assoc.ContainsAssoc;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;

/*
 * Element of the repository. Base class for all elements.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldNameConstants
@FieldDefaults(level = PRIVATE)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "element", schema = "repo")
public class Element extends AbstractModifiableEntity {
	
	@Column(nullable = false)
	UUID ref;												// element unique reference
	
	@Column(length = 255, nullable = false)
	String name;											// element name
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "childElementId")
	Set<ContainsAssoc> parentsAssoc = new HashSet<>();		// parents of the element
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentElementId")
	Set<ContainsAssoc> childrenAssoc = new HashSet<>();		// children of the element
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Element2Feature", schema = "repo",
		joinColumns = { @JoinColumn(name = "element_id") }, 
		inverseJoinColumns = { @JoinColumn(name ="feature_id") })
	Set<Feature> features = new HashSet<>();				// element features
	
	@Transient
	List<Element> childrenElements;
 	
	
	public Element(int id) {
		super(id);
	}
	
	public String getTypeName() {
		return "Element";
	}
	
	public void addFeature(Feature feature) {
		features.add(feature);
	}
	
	public Element getParentElement() {
		var iterator = parentsAssoc.iterator();
		if(iterator.hasNext()) {
			return iterator.next().getParentElement();
		}
		return null;
	}
	
	public Element copy() {
		throw new UnsupportedOperationException();
	}
	
	protected void fillCopy(Element copy) {
		copy.setName(name);
	}
}
