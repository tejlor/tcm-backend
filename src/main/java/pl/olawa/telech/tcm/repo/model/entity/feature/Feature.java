package pl.olawa.telech.tcm.repo.model.entity.feature;

import static lombok.AccessLevel.PRIVATE;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import pl.olawa.telech.tcm.commons.model.entity.AbstractModifiableEntity;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

/*
 * Element feature. Set of extra attributes attached to the element.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldNameConstants
@FieldDefaults(level = PRIVATE)
@Table(name = "feature", schema = "repo")
public class Feature extends AbstractModifiableEntity {
	
	@Column(length = 32, nullable = false)
	String name;											// display name
	
	@Column(length = 32, nullable = false)
	String code;											// short name
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "feature")
	Set<FeatureAttribute> attributes = new HashSet<>();		// feature attributes
	
	@ManyToMany(mappedBy = "features")
	Set<Element> elements = new HashSet<>();				// elements with this feature
 	
	
	public Feature(int id) {
		super(id);
	}
	
	public void setAttributes(Collection<FeatureAttribute> attributes) {
		for(FeatureAttribute attribute : attributes) {
			addAttribute(attribute);
		}
	}
	
	public void addAttribute(FeatureAttribute attribute) {
		attributes.add(attribute);
		attribute.setFeature(this);
	}
}
