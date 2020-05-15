package pl.olawa.telech.tcm.controller;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pl.olawa.telech.tcm.logic.FileLogic;
import pl.olawa.telech.tcm.model.dto.entity.FileDto;
import pl.olawa.telech.tcm.model.entity.element.File;


@RestController
@RequestMapping("/files")
public class FileController extends AbstractController {

	@Autowired
	private FileLogic fileLogic;


	/*
	 * Return file info.
	 */
	@RequestMapping(value = "/{ref:[a-z0-9-]{36}}", method = RequestMethod.GET)
	public FileDto get(
			@PathVariable String ref) {

		return new FileDto(fileLogic.loadByRef(UUID.fromString(ref)));
	}
	
	/*
	 * Downloads file.
	 */
	@RequestMapping(value = "/{ref:[a-z0-9-]{36}}/content", method = RequestMethod.GET)
	public ResponseEntity<Resource> content(
			@PathVariable String ref) {

		Pair<File, Resource> file = fileLogic.downloadContent(UUID.fromString(ref));
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getKey().getName() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, file.getKey().getMimeType())
				.body(file.getValue());
	}
	
	/*
	 * Downloads file.
	 */
	@RequestMapping(value = "/{ref:[a-z0-9-]{36}}/preview", method = RequestMethod.GET)
	public ResponseEntity<Resource> preview(
			@PathVariable String ref) {

		Pair<File, Resource> file = fileLogic.downloadPreview(UUID.fromString(ref));
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getKey().getName() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, file.getKey().getMimeType())
				.body(file.getValue());
	}
	
	/*
	 * Uploads new files to specified directory.
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public List<FileDto> upload( 
			@RequestParam MultipartFile[] file,
			@RequestParam String dirRef) {
		
		return FileDto.toFileDtoList(fileLogic.upload(file, UUID.fromString(dirRef)));
	}



}