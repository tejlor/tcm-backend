package pl.olawa.telech.tcm.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pl.olawa.telech.tcm.logic.FileLogic;
import pl.olawa.telech.tcm.model.dto.entity.FileDto;
import pl.olawa.telech.tcm.model.entity.element.FileEl;
import pl.olawa.telech.tcm.utils.TUtils;


@RestController
@RequestMapping("/files")
public class FileController extends AbstractController {

	@Autowired
	private FileLogic fileLogic;


	/*
	 * Returns file info.
	 */
	@RequestMapping(value = "/{ref:" + AbstractController.REF + "}", method = RequestMethod.GET)
	public FileDto get(
			@PathVariable String ref) {

		return new FileDto(fileLogic.loadByRef(UUID.fromString(ref)));
	}
	
	/*
	 * Downloads file preview content.
	 */
	@RequestMapping(value = "/{ref:" + AbstractController.REF + "}/preview", method = RequestMethod.GET)
	public ResponseEntity<Resource> preview(
			@PathVariable String ref) {

		Pair<FileEl, Resource> file = fileLogic.downloadPreview(UUID.fromString(ref));
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getKey().getName() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, file.getKey().getMimeType())
				.body(file.getValue());
	}
	
	/*
	 * Downloads file content.
	 */
	@RequestMapping(value = "/{ref:" + AbstractController.REF + "}/content", method = RequestMethod.GET)
	public ResponseEntity<Resource> content(
			@PathVariable String ref) {

		Pair<FileEl, Resource> file = fileLogic.downloadContent(UUID.fromString(ref));
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getKey().getName() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, file.getKey().getMimeType())
				.body(file.getValue());
	}
	
	/*
	 * Downloads many files as zip.
	 */
	@RequestMapping(value = "/zip", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadAsZip(
			@RequestParam List<String> refs){

		List<UUID> uuids = refs.stream().map(ref -> UUID.fromString(ref)).collect(Collectors.toList());
		Resource file = fileLogic.downloadAsZip(uuids);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"TCM_Files.zip\"")
				.header(HttpHeaders.CONTENT_TYPE, "application/zip")
				.body(file);
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