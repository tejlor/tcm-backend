package pl.olawa.telech.tcm.repo.builder.feature;

import static lombok.AccessLevel.PRIVATE;

import java.util.Set;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.builder.AbstractModifiableBuilder;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class FeatureBuilder extends AbstractModifiableBuilder<Feature> {
	
	String name;
	String code;
	Set<FeatureAttribute> attributes;
	
	@Override
	public Feature build() {
		var obj = new Feature();
		super.fill(obj);
		obj.setName(name);
		obj.setCode(code);
		obj.setAttributes(attributes);
		return obj;	
	}
}
