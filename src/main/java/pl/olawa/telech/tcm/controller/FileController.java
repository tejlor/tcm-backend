package pl.olawa.telech.tcm.controller;

import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public void upload( 
			@RequestParam("file") MultipartFile file,
			@RequestParam("dirRef") String dirRef) {
		
		fileLogic.upload(file, UUID.fromString(dirRef));
	}

	@RequestMapping(value = "download/{ref}", method = RequestMethod.GET)
	public ResponseEntity<Resource> serveFile(
			@PathVariable String ref) {

		Resource file = fileLogic.download(UUID.fromString(ref));
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

}