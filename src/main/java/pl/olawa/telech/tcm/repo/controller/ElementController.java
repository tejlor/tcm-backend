package pl.olawa.telech.tcm.repo.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.controller.AbstractController;
import pl.olawa.telech.tcm.commons.model.dto.TableDataDto;
import pl.olawa.telech.tcm.commons.model.dto.TreeNodeDto;
import pl.olawa.telech.tcm.commons.model.shared.Path;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.commons.utils.TUtils;
import pl.olawa.telech.tcm.repo.logic.ElementLogicImpl;
import pl.olawa.telech.tcm.repo.model.dto.ElementDto;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;


@RestController
@RequestMapping("/elements")
@FieldDefaults(level = PRIVATE)
public class ElementController extends AbstractController {

	@Autowired
	ElementLogicImpl elementLogic;
	
	
	/*
	 * Returns element info.
	 */
	@RequestMapping(value = "/{ref:" + REF + "}", method = GET)
	public ElementDto get(
			@PathVariable UUID ref) {

		return ElementDto.toDto(elementLogic.loadByRef(ref));
	}
	
	/*
	 * Returns absolute path of element.
	 */
	@RequestMapping(value = "/{ref:" + REF + "}/path", method = GET)
	public Path path(
		@PathVariable UUID ref){
				
		return elementLogic.loadAbsolutePath(ref);
	}
	
	/*
	 * Returns children of element for tree.
	 */
	@RequestMapping(value = "/{parentRef:" + REF + "}/childrenTree", method = GET)
	public List<TreeNodeDto> childrenTree(
		@PathVariable UUID parentRef){
				
		return elementLogic.loadChildren(parentRef).stream()
				.map(e -> new TreeNodeDto(e))
				.sorted()
				.collect(Collectors.toList());
	}
	
	/*
	 * Returns parents of element for tree.
	 */
	@RequestMapping(value = "/{ref:" + REF + "}/parentsTree", method = GET)
	public TreeNodeDto parentsTree(
		@PathVariable UUID ref){
				
		return new TreeNodeDto(elementLogic.loadParentsTree(ref));
	}
	
	/*
	 * Returns children of element for table.
	 */
	@RequestMapping(value = "/{ref:" + REF + "}/childrenTable", method = GET)
	public TableDataDto<ElementDto> childrenTable(
		@PathVariable UUID ref,	
		@RequestParam(required = false) Integer pageNo,
		@RequestParam(required = false) Integer pageSize,
		@RequestParam(required = false) String filter,
		@RequestParam(required = false) String sortBy,
		@RequestParam(required = false) Boolean sortAsc){
		
		var tableParams = new TableParams(pageNo, pageSize, filter, sortBy, sortAsc);		
		Pair<List<Element>, Integer> result = elementLogic.loadChildren(ref, tableParams); 	
		var table = new TableDataDto<ElementDto>(tableParams);
		table.setRows(ElementDto.toDtoList(result.getKey()));
		table.setCount(result.getValue());		
		return table;
	}

	/*
	 * Rename element.
	 */
	@RequestMapping(value = "/rename", method = POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void rename(
		@RequestParam String ref,
		@RequestParam String newName){
				
		elementLogic.rename(TUtils.parseUUID(ref), newName);
	}
	
	/*
	 * Moves elements.
	 */
	@RequestMapping(value = "/move", method = POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void move(
		@RequestParam List<String> refs,
		@RequestParam String newParentRef){
				
		elementLogic.move(TUtils.parseUUIDs(refs), TUtils.parseUUID(newParentRef));
	}
	
	/*
	 * Copies elements.
	 */
	@RequestMapping(value = "/copy", method = POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void copy(
		@RequestParam List<String> refs,		
		@RequestParam String newParentRef){
				
		elementLogic.copy(TUtils.parseUUIDs(refs), TUtils.parseUUID(newParentRef));
	}
	
	/*
	 * Deletes elements (moves to trash).
	 */
	@RequestMapping(value = "/{ref:" + REF + "}", method = DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remove(
		@PathVariable List<String> refs){
				
		elementLogic.remove(TUtils.parseUUIDs(refs));
	}
}
