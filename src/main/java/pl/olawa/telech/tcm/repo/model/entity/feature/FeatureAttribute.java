package pl.olawa.telech.tcm.repo.model.entity.feature;

import static lombok.AccessLevel.PRIVATE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.entity.AbstractModifiableEntity;
import pl.olawa.telech.tcm.repo.model.enums.FeatureAttributeType;

/*
 * Definition of feature attribute.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Table(name = "feature_attribute", schema = "repo")
public class FeatureAttribute extends AbstractModifiableEntity {
	
	public static final String PROP_FEATURE = "feature"; 
	
	@Column(length = 32, nullable = false)
	String name;							// display name
	
	@Enumerated(EnumType.STRING)
	@Column(length = 16, nullable = false)
	FeatureAttributeType type;				// type of value
	
	@Column
	boolean required;						// is value required
	
	@Column(insertable = false, updatable = false)
	Integer featureId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "featureId")
	Feature feature;						// feature
 	
	
	public FeatureAttribute(int id) {
		super(id);
	}
	
	public void setFeature(Feature feature) {
		this.feature = feature;
		this.featureId = feature != null ? feature.getId() : null;
	}
}
