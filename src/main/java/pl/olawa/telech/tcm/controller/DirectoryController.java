package pl.olawa.telech.tcm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.olawa.telech.tcm.dto.entity.DirectoryDto;
import pl.olawa.telech.tcm.logic.DirectoryLogic;


@RestController
@RequestMapping("/directories")
public class DirectoryController extends AbstractController {

	@Autowired
	private DirectoryLogic directoryLogic;

	
	/*
	 * Creates new directory.
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public DirectoryDto create(
			@RequestBody(required = true) DirectoryDto directory) {
		
		return new DirectoryDto(directoryLogic.create(directory.toModel()));
	}
	


}