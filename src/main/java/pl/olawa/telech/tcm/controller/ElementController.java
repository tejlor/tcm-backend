package pl.olawa.telech.tcm.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pl.olawa.telech.tcm.dto.entity.ElementDto;
import pl.olawa.telech.tcm.logic.ElementLogic;
import pl.olawa.telech.tcm.model.shared.TableParams;


@RestController
@RequestMapping("/elements")
public class ElementController extends AbstractController {

	@Autowired
	private ElementLogic elementLogic;
	
	/*
	 * List children of element.
	 */
	@RequestMapping(value = "/{parentRef:[a-z0-9-]{36}}/children", method = RequestMethod.GET)
	public List<ElementDto> children(
		@PathVariable String parentRef,	
		@RequestParam(required = false) Integer pageNo,
		@RequestParam(required = false) Integer pageSize,
		@RequestParam(required = false) String filter,
		@RequestParam(required = false) String sortBy,
		@RequestParam(required = false) Boolean sortAsc){
		
		TableParams tableParams = new TableParams(pageNo, pageSize, filter, sortBy, sortAsc);
		
		return ElementDto.toDtoList(elementLogic.loadByParent(UUID.fromString(parentRef), tableParams));
	}

}