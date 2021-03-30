package pl.olawa.telech.tcm.repo.builder.feature;

import static lombok.AccessLevel.PRIVATE;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.builder.AbstractModifiableBuilder;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttributeValue;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class FeatureAttributeValueBuilder extends AbstractModifiableBuilder<FeatureAttributeValue> {
	
	FeatureAttribute attribute;
	Element element;
	Object value;
	
	@Override
	public FeatureAttributeValue build() {
		var obj = new FeatureAttributeValue();
		super.fill(obj);
		obj.setFeatureAttribute(attribute);
		obj.setElement(element);
		obj.setValue(value);
		return obj;	
	}
}
