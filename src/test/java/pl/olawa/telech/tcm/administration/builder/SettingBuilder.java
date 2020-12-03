package pl.olawa.telech.tcm.administration.builder;

import static lombok.AccessLevel.PRIVATE;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.administration.model.entity.Setting;
import pl.olawa.telech.tcm.commons.builder.AbstractBuilder;

@Setter
@Accessors(chain = true, fluent = true)
@FieldDefaults(level = PRIVATE)
public class SettingBuilder extends AbstractBuilder<Setting> {
	
	String name = "PARAM_1";
	String value = "true";
	
	@Override
	public Setting build() {
		var setting = new Setting();
		setting.setName(name);
		setting.setValue(value);
		return setting;	
	}
}
