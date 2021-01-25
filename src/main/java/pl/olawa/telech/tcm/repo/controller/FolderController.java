package pl.olawa.telech.tcm.repo.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.controller.AbstractController;
import pl.olawa.telech.tcm.repo.logic.FolderLogicImpl;
import pl.olawa.telech.tcm.repo.model.dto.FolderDto;


@RestController
@RequestMapping("/folders")
@FieldDefaults(level = PRIVATE)
public class FolderController extends AbstractController {

	@Autowired
	FolderLogicImpl folderLogic;

	/*
	 * Returns folder info.
	 */
	@RequestMapping(value = "{ref:" + REF + "}", method = GET)
	public FolderDto get(
			@PathVariable String ref) {

		return new FolderDto(folderLogic.loadByRef(UUID.fromString(ref)));
	}
	
	/*
	 * Creates new folder.
	 */
	@RequestMapping(value = "", method = POST)
	public FolderDto create(
			@RequestBody FolderDto folder) {
		
		return new FolderDto(folderLogic.create(UUID.fromString(folder.getParentRef()), folder.toModel()));
	}
}
