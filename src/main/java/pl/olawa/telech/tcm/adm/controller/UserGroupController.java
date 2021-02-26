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
import pl.olawa.telech.tcm.adm.logic.UserGroupLogicImpl;
import pl.olawa.telech.tcm.adm.model.dto.UserGroupDto;
import pl.olawa.telech.tcm.adm.model.entity.UserGroup;
import pl.olawa.telech.tcm.commons.controller.AbstractController;
import pl.olawa.telech.tcm.commons.model.dto.TableDataDto;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.commons.utils.TUtils;


@RestController
@RequestMapping("/userGroups")
@FieldDefaults(level = PRIVATE)
public class UserGroupController extends AbstractController {

	@Autowired
	UserGroupLogicImpl userGroupLogic;

	/*
	 * Returns all user groups.
	 */
	@RequestMapping(value = "", method = GET)
	public List<UserGroupDto> getAll() {
		
		return UserGroupDto.toDtoList(userGroupLogic.loadAll());
	}
	
	/*
	 * Returns all user groups for table.
	 */
	@RequestMapping(value = "/table", method = GET)
	public TableDataDto<UserGroupDto> getTable(
		@RequestParam(required = false) Integer pageNo,
		@RequestParam(required = false) Integer pageSize,
		@RequestParam(required = false) String filter,
		@RequestParam(required = false) String sortBy,
		@RequestParam(required = false) Boolean sortAsc){
		
		var tableParams = new TableParams(pageNo, pageSize, filter, sortBy, sortAsc);		
		Pair<List<UserGroup>, Integer> result = userGroupLogic.loadTable(tableParams); 	
		var table = new TableDataDto<UserGroupDto>(tableParams);
		table.setRows(UserGroupDto.toDtoList(result.getKey()));
		table.setCount(result.getValue());		
		return table;
	}
	
	/*
	 * Export all user groups to xlsx.
	 */
	@RequestMapping(value = "/xlsx", method = GET)
	public ResponseEntity<ByteArrayResource> exportToXlsx() {
		
         byte[] bytes = userGroupLogic.exportToXlsx().toByteArray();
         var resource = new ByteArrayResource(bytes);
         return ResponseEntity.ok().body(resource);
	}
	
	/*
	 * Creates new user group.
	 */
	@RequestMapping(value = "", method = POST)
	public UserGroupDto create(
			@RequestBody UserGroupDto userGroup) {
		
		return new UserGroupDto(userGroupLogic.create(userGroup.toModel()));
	}
	
	/*
	 * Updates user group.
	 */
	@RequestMapping(value = "/{id:" + ID +"}", method = PUT)
	public UserGroupDto update(
			@PathVariable int id,
			@RequestBody UserGroupDto userGroup) {
		
		TUtils.assertDtoId(id, userGroup);
		return new UserGroupDto(userGroupLogic.update(id, userGroup.toModel()));
	}
}
