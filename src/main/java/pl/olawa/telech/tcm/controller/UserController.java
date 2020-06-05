package pl.olawa.telech.tcm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.olawa.telech.tcm.logic.UserLogic;
import pl.olawa.telech.tcm.model.dto.entity.UserDto;


@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {

	@Autowired
	private UserLogic userLogic;

	
	/*
	 * Return all users.
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<UserDto> getAll() {
		
		return UserDto.toDtoList(userLogic.loadAll());
	}


}