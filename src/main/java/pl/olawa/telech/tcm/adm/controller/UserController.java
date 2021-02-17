package pl.olawa.telech.tcm.adm.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.logic.UserLogicImpl;
import pl.olawa.telech.tcm.adm.model.dto.UserDto;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.commons.controller.AbstractController;
import pl.olawa.telech.tcm.commons.model.dto.TableDataDto;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.commons.utils.TUtils;


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
	
	/*
	 * Returns all users for table.
	 */
	@RequestMapping(value = "/table", method = GET)
	public TableDataDto<UserDto> getTable(
		@RequestParam(required = false) Integer pageNo,
		@RequestParam(required = false) Integer pageSize,
		@RequestParam(required = false) String filter,
		@RequestParam(required = false) String sortBy,
		@RequestParam(required = false) Boolean sortAsc){
		
		var tableParams = new TableParams(pageNo, pageSize, filter, sortBy, sortAsc);		
		Pair<List<User>, Integer> result = userLogic.loadTable(tableParams); 	
		var table = new TableDataDto<UserDto>(tableParams);
		table.setRows(UserDto.toDtoList(result.getKey()));
		table.setCount(result.getValue());		
		return table;
	}
	
	/*
	 * Export all users to xlsx.
	 */
	@RequestMapping(value = "/xlsx", method = GET)
	public ResponseEntity<ByteArrayResource> exportToXlsx() {
		
         byte[] bytes = userLogic.exportToXlsx().toByteArray();
         var resource = new ByteArrayResource(bytes);
         return ResponseEntity.ok().body(resource);
	}
	
	/*
	 * Creates new user.
	 */
	@RequestMapping(value = "", method = POST)
	public UserDto create(
			@RequestBody UserDto user) {
		
		return new UserDto(userLogic.create(user.toModel()));
	}
	
	/*
	 * Updates user.
	 */
	@RequestMapping(value = "/{id:" + ID +"}", method = PUT)
	public UserDto update(
			@PathVariable int id,
			@RequestBody UserDto user) {
		
		TUtils.assertDtoId(id, user);
		return new UserDto(userLogic.update(id, user.toModel()));
	}
}
