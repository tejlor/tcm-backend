package pl.olawa.telech.tcm.adm.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.builder.SettingBuilder;
import pl.olawa.telech.tcm.adm.model.dto.SettingDto;
import pl.olawa.telech.tcm.adm.model.entity.Setting;
import pl.olawa.telech.tcm.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class SettingControllerTest extends BaseTest {

	@Autowired
	SettingController settingController;

	@Test
	@Transactional
	public void getSafe() {
		// given
		var settingList = new ArrayList<Setting>();
		settingList.add(setupSetting(Setting.ROOT_REF, "rootRef"));
		settingList.add(setupSetting(Setting.TRASH_REF, "trashRef"));
		settingList.add(setupSetting("UNSAFE_SETTING", "unsafe_value"));
		flush();	
		// when
		List<SettingDto> result = settingController.getSafe();	
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		assertThat(result.size()).isEqualTo(2);
		for(int i = 0; i < 2; i++) {
			assertSetting(result.get(i), settingList.get(i));
		}
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void assertSetting(SettingDto settingDto, Setting setting) {
		assertThat(settingDto.getId()).isEqualTo(setting.getId());
		assertThat(settingDto.getName()).isEqualTo(setting.getName());
		assertThat(settingDto.getValue()).isEqualTo(setting.getValue());
	}
	
	private Setting setupSetting(String name, String value) {
		return new SettingBuilder()
			.name(name)
			.value(value)
			.saveAndReload(entityManager);
	}
}
