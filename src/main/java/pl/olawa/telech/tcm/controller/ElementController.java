package pl.olawa.telech.tcm.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.olawa.telech.tcm.logic.ElementLogic;
import pl.olawa.telech.tcm.model.dto.TableDataDto;
import pl.olawa.telech.tcm.model.dto.TreeNodeDto;
import pl.olawa.telech.tcm.model.dto.entity.ElementDto;
import pl.olawa.telech.tcm.model.entity.element.Element;
import pl.olawa.telech.tcm.model.shared.Path;
import pl.olawa.telech.tcm.model.shared.TableParams;
import pl.olawa.telech.tcm.utils.TUtils;


@RestController
@RequestMapping("/elements")
public class ElementController extends AbstractController {

	@Autowired
	private ElementLogic elementLogic;
	
	
	/*
	 * Returns element info.
	 */
	@RequestMapping(value = "/{ref:" + AbstractController.REF + "}", method = RequestMethod.GET)
	public ElementDto get(
			@PathVariable String ref) {

		return ElementDto.toDto(elementLogic.loadByRef(UUID.fromString(ref)));
	}
	
	/*
	 * Returns absolute path of element.
	 */
	@RequestMapping(value = "/{elementRef:" + AbstractController.REF + "}/path", method = RequestMethod.GET)
	public Path path(
		@PathVariable String elementRef){
				
		return elementLogic.loadAbsolutePath((UUID.fromString(elementRef)));
	}
	
	/*
	 * Returns children of element for tree.
	 */
	@RequestMapping(value = "/{parentRef:" + AbstractController.REF + "}/childrenTree", method = RequestMethod.GET)
	public List<TreeNodeDto> childrenTree(
		@PathVariable String parentRef){
				
		return elementLogic.loadChildren(UUID.fromString(parentRef)).stream()
				.map(e -> new TreeNodeDto(e))
				.sorted()
				.collect(Collectors.toList());
	}
	
	/*
	 * Returns parents of element for tree.
	 */
	@RequestMapping(value = "/{elementRef:" + AbstractController.REF + "}/parentsTree", method = RequestMethod.GET)
	public TreeNodeDto parentsTree(
		@PathVariable String elementRef){
				
		return new TreeNodeDto(elementLogic.loadParentsTree((UUID.fromString(elementRef))));
	}
	
	/*
	 * Returns children of element for table.
	 */
	@RequestMapping(value = "/{parentRef:" + AbstractController.REF + "}/childrenTable", method = RequestMethod.GET)
	public TableDataDto<ElementDto> childrenTable(
		@PathVariable String parentRef,	
		@RequestParam(required = false) Integer pageNo,
		@RequestParam(required = false) Integer pageSize,
		@RequestParam(required = false) String filter,
		@RequestParam(required = false) String sortBy,
		@RequestParam(required = false) Boolean sortAsc){
		
		TableParams tableParams = new TableParams(pageNo, pageSize, filter, sortBy, sortAsc);
		
		Pair<List<Element>, Integer> result = elementLogic.loadChildren(UUID.fromString(parentRef), tableParams); 
		
		TableDataDto<ElementDto> table = new TableDataDto<>(tableParams);
		table.setRows(ElementDto.toDtoList(result.getKey()));
		table.setCount(result.getValue());
		
		return table;
	}

	/*
	 * Rename element.
	 */
	@RequestMapping(value = "/rename", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void rename(
		@RequestParam String ref,
		@RequestParam String newName){
				
		elementLogic.rename(TUtils.parseUUID(ref), newName);
	}
	
	/*
	 * Moves elements.
	 */
	@RequestMapping(value = "/move", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void move(
		@RequestParam String newParentRef,
		@RequestParam List<String> refs){
				
		elementLogic.move(TUtils.parseUUID(newParentRef), TUtils.parseUUIDs(refs));
	}
	
	/*
	 * Copies elements.
	 */
	@RequestMapping(value = "/copy", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void copy(
		@RequestParam String newParentRef,
		@RequestParam List<String> refs){
				
		elementLogic.copy(TUtils.parseUUID(newParentRef), TUtils.parseUUIDs(refs));
	}
	
	/*
	 * Deletes elements (moves to trash).
	 */
	@RequestMapping(value = "/{ref:" + AbstractController.REF + "}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(
		@PathVariable List<String> refs){
				
		elementLogic.delete(TUtils.parseUUIDs(refs));
	}
}