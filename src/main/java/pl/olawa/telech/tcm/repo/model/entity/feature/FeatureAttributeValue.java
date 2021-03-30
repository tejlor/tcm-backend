package pl.olawa.telech.tcm.repo.model.entity.feature;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.entity.AbstractModifiableEntity;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

/*
 * Feature attribute value of connected element.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Table(name = "feature_attribute_value", schema = "repo")
public class FeatureAttributeValue extends AbstractModifiableEntity {
	
	public static final String PROP_FEATURE_ATTRIBUTE = "featureAttribute";
	
	@Column(insertable = false, updatable = false)
	Integer featureAttributeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "featureAttributeId")
	FeatureAttribute featureAttribute;		// feature attribute definition
	
	@Column(insertable = false, updatable = false)
	Integer elementId;					
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "elementId")
	Element element;						// element
	
	@Column 
	Integer valueInt;						// int value 
	
	@Column
	float valueFloat;						// float value
	
	@Column
	Boolean valueBool;						// boolean value
	
	@Column
	BigDecimal valueDec;					// decimal value (with 2 decimal digits)
	
	@Column(length = 255)
	String valueString;						// short text value (up to 255 chars)
	
	@Column
	String valueText;						// long text value
	
	@Column
	LocalDate valueDate;					// date value
	
	@Column(columnDefinition = "blob")
	LocalDateTime valueTime;				// date with time value
 	
	
	public FeatureAttributeValue(int id) {
		super(id);
	}
	
	public void setFeatureAttribute(FeatureAttribute attribute) {
		this.featureAttribute = attribute;
		this.featureAttributeId = attribute != null ? attribute.getId() : null;
	}
	
	public void setElement(Element element) {
		this.element = element;
		this.elementId = element != null ? element.getId() : null;
	}
	
	public Object getValue() {
		switch(featureAttribute.getType()) {
			case INT: 	 return valueInt;
			case FLOAT:  return valueFloat;
			case DEC: 	 return valueDec;
			case BOOL: 	 return valueBool;
			case STRING: return valueString;
			case TEXT: 	 return valueText;
			case DATE: 	 return valueDate;
			case TIME: 	 return valueTime;
			default:	 throw new IllegalArgumentException();
		}
	}
	
	public void setValue(Object value) {
		switch(featureAttribute.getType()) {
			case INT: 	 setValueInt((Integer) value);
			case FLOAT:  setValueFloat((float) value);
			case DEC: 	 setValueDec((BigDecimal) value);
			case BOOL: 	 setValueBool((Boolean) value);
			case STRING: setValueString((String) value);
			case TEXT: 	 setValueText((String) value);
			case DATE: 	 setValueDate((LocalDate) value);
			case TIME: 	 setValueTime((LocalDateTime) value);
			default:	 throw new IllegalArgumentException();
		}
	}
}
