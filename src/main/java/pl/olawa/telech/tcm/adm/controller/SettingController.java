package pl.olawa.telech.tcm.adm.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.logic.SettingLogicImpl;
import pl.olawa.telech.tcm.adm.model.dto.SettingDto;
import pl.olawa.telech.tcm.commons.controller.AbstractController;


@RestController
@RequestMapping("/settings")
@FieldDefaults(level = PRIVATE)
public class SettingController extends AbstractController {

	@Autowired
	SettingLogicImpl settingLogic;

	/*
	 * Returns safe settings for web app.
	 */
	@RequestMapping(value = "safe", method = GET)
	public List<SettingDto> getSafe() {
		
		return SettingDto.toDtoList(settingLogic.loadSafe());
	}
}
