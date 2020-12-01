package pl.olawa.telech.tcm.repo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.olawa.telech.tcm.commons.controller.AbstractController;
import pl.olawa.telech.tcm.repo.logic.FolderLogic;
import pl.olawa.telech.tcm.repo.model.dto.FolderDto;


@RestController
@RequestMapping("/folders")
public class FolderController extends AbstractController {

	@Autowired
	private FolderLogic folderLogic;

	/*
	 * Return folder info.
	 */
	@RequestMapping(value = "{ref:" + AbstractController.REF + "}", method = RequestMethod.GET)
	public FolderDto get(
			@PathVariable String ref) {

		return new FolderDto(folderLogic.loadByRef(UUID.fromString(ref)));
	}
	
	/*
	 * Creates new folder.
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public FolderDto create(
			@RequestBody FolderDto folder) {
		
		return new FolderDto(folderLogic.create(folder.toModel()));
	}
	
}