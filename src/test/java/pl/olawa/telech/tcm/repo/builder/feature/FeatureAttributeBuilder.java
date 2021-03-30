package pl.olawa.telech.tcm.repo.builder.feature;

import static lombok.AccessLevel.PRIVATE;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.builder.AbstractModifiableBuilder;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;
import pl.olawa.telech.tcm.repo.model.enums.FeatureAttributeType;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class FeatureAttributeBuilder extends AbstractModifiableBuilder<FeatureAttribute> {
	
	String name;
	FeatureAttributeType type;
	boolean required = true;
	Feature feature;
	
	@Override
	public FeatureAttribute build() {
		var obj = new FeatureAttribute();
		super.fill(obj);
		obj.setName(name);
		obj.setType(type);
		obj.setRequired(required);
		obj.setFeature(feature);
		return obj;	
	}
}
