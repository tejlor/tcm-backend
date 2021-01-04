package pl.olawa.telech.tcm.repo.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.controller.AbstractController;
import pl.olawa.telech.tcm.repo.logic.FileLogic;
import pl.olawa.telech.tcm.repo.model.dto.FileDto;
import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;

@RestController
@RequestMapping("/files")
@FieldDefaults(level = PRIVATE)
public class FileController extends AbstractController {

	@Autowired
	FileLogic fileLogic;

	/*
	 * Returns file info.
	 */
	@RequestMapping(value = "/{ref:" + REF + "}", method = GET)
	public FileDto get(
			@PathVariable String ref) {

		return new FileDto(fileLogic.loadByRef(UUID.fromString(ref)));
	}
	
	/*
	 * Downloads file preview content.
	 */
	@RequestMapping(value = "/{ref:" + REF + "}/preview", method = GET)
	public ResponseEntity<Resource> preview(
			@PathVariable String ref) {

		Pair<FileEl, Resource> file = fileLogic.downloadPreview(UUID.fromString(ref));
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getKey().getName() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, file.getKey().getPreviewMimeType())
				.body(file.getValue());
	}
	
	/*
	 * Downloads file content.
	 */
	@RequestMapping(value = "/{ref:" + REF + "}/content", method = GET)
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
	@RequestMapping(value = "/zip", method = GET)
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
	@RequestMapping(value = "", method = POST)
	public List<FileDto> upload( 
			@RequestParam MultipartFile[] files,
			@RequestParam String dirRef) {
		
		return FileDto.toFileDtoList(fileLogic.upload(files, UUID.fromString(dirRef)));
	}
}
