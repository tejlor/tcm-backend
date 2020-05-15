package pl.olawa.telech.tcm.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.olawa.telech.tcm.logic.ElementLogic;
import pl.olawa.telech.tcm.model.dto.TableDataDto;
import pl.olawa.telech.tcm.model.dto.TreeNodeDto;
import pl.olawa.telech.tcm.model.dto.entity.ElementDto;
import pl.olawa.telech.tcm.model.shared.Path;
import pl.olawa.telech.tcm.model.shared.TableParams;


@RestController
@RequestMapping("/elements")
public class ElementController extends AbstractController {

	@Autowired
	private ElementLogic elementLogic;
	
	
	/*
	 * Return element info.
	 */
	@RequestMapping(value = AbstractController.ref, method = RequestMethod.GET)
	public ElementDto get(
			@PathVariable String ref) {

		return ElementDto.toDto(elementLogic.loadByRef(UUID.fromString(ref)));
	}
	
	/*
	 * Returns children of element for tree.
	 */
	@RequestMapping(value = "/{parentRef:[a-z0-9-]{36}}/childrenTree", method = RequestMethod.GET)
	public List<TreeNodeDto> childrenTree(
		@PathVariable String parentRef){
				
		return elementLogic.loadChildren(UUID.fromString(parentRef)).stream()
				.map(e -> new TreeNodeDto(e))
				.sorted()
				.collect(Collectors.toList());
	}
	
	/*
	 * Returns children of element for tree.
	 */
	@RequestMapping(value = "/{elementRef:[a-z0-9-]{36}}/parentsTree", method = RequestMethod.GET)
	public TreeNodeDto parentsTree(
		@PathVariable String elementRef){
				
		return new TreeNodeDto(elementLogic.loadParentsTree((UUID.fromString(elementRef))));
	}
	
	/*
	 * Returns children of element for table.
	 */
	@RequestMapping(value = "/{parentRef:[a-z0-9-]{36}}/childrenTable", method = RequestMethod.GET)
	public TableDataDto<ElementDto> childrenTable(
		@PathVariable String parentRef,	
		@RequestParam(required = false) Integer pageNo,
		@RequestParam(required = false) Integer pageSize,
		@RequestParam(required = false) String filter,
		@RequestParam(required = false) String sortBy,
		@RequestParam(required = false) Boolean sortAsc){
		
		TableParams tableParams = new TableParams(pageNo, pageSize, filter, sortBy, sortAsc);
		
		var result = elementLogic.loadChildren(UUID.fromString(parentRef), tableParams); 
		
		TableDataDto<ElementDto> table = new TableDataDto<>(tableParams);
		table.setRows(ElementDto.toDtoList(result.getKey()));
		table.setCount(result.getValue());
		
		return table;
	}
	
	/*
	 * Returns absolute path of element.
	 */
	@RequestMapping(value = "/{elementRef:[a-z0-9-]{36}}/path", method = RequestMethod.GET)
	public Path path(
		@PathVariable String elementRef){
				
		return elementLogic.loadAbsolutePath((UUID.fromString(elementRef)));
	}

}