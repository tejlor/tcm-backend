package pl.olawa.telech.tcm.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pl.olawa.telech.tcm.logic.FileLogic;


@RestController
@RequestMapping("/files")
public class FileController extends AbstractController {

	@Autowired
	private FileLogic fileLogic;

	
	/*
	 * 
	 */
	@RequestMapping(value = "upload", method = RequestMethod.GET)
	public void upload( 
			@RequestParam("file") MultipartFile file,
			@RequestParam("dirRef") String dirRef) {
		
		fileLogic.upload(file, UUID.fromString(dirRef));
	}


}