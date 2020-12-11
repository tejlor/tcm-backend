package pl.olawa.telech.tcm.adm.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.logic.UserLogicImpl;
import pl.olawa.telech.tcm.adm.model.dto.UserDto;
import pl.olawa.telech.tcm.commons.controller.AbstractController;


@RestController
@RequestMapping("/users")
@FieldDefaults(level = PRIVATE)
public class UserController extends AbstractController {

	@Autowired
	UserLogicImpl userLogic;

	/*
	 * Returns all users.
	 */
	@RequestMapping(value = "", method = GET)
	public List<UserDto> getAll() {
		
		return UserDto.toDtoList(userLogic.loadAll());
	}
}
