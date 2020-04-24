package pl.olawa.telech.tcm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.olawa.telech.tcm.logic.FolderLogic;
import pl.olawa.telech.tcm.model.dto.entity.FolderDto;


@RestController
@RequestMapping("/folders")
public class FolderController extends AbstractController {

	@Autowired
	private FolderLogic directoryLogic;

	
	/*
	 * Creates new folder.
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public FolderDto create(
			@RequestBody(required = true) FolderDto folder) {
		
		return new FolderDto(directoryLogic.create(folder.toModel()));
	}
	


}