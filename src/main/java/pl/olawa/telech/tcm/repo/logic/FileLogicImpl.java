package pl.olawa.telech.tcm.repo.logic;

import static lombok.AccessLevel.PRIVATE;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.commons.model.exception.TcmException;
import pl.olawa.telech.tcm.repo.dao.FileDAO;
import pl.olawa.telech.tcm.repo.logic.helper.FilePreviewGenerator;
import pl.olawa.telech.tcm.repo.logic.interfaces.ContainsAssocLogic;
import pl.olawa.telech.tcm.repo.logic.interfaces.ElementLogic;
import pl.olawa.telech.tcm.repo.logic.interfaces.FileLogic;
import pl.olawa.telech.tcm.repo.logic.service.interfaces.DiskService;
import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;


@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class FileLogicImpl extends AbstractLogicImpl<FileEl> implements FileLogic {

	FileDAO dao;
	
	@Autowired
	DiskService diskService;
	@Autowired
	FilePreviewGenerator filePreviewGenerator;
	
	@Autowired
	ContainsAssocLogic containsAssocLogic;	
	@Autowired
	ElementLogic elementLogic;	
	
	
	public FileLogicImpl(FileDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public FileEl loadByRef(UUID ref) {
		return dao.findByRef(ref);
	}
	
	@Override
	public List<FileEl> upload(MultipartFile[] uploadedFiles, UUID dirRef) {
		return Arrays.stream(uploadedFiles)
				.map(part -> upload(part, dirRef))
				.collect(Collectors.toList());
	}
	
	@Override
	public Pair<FileEl, Resource> downloadContent(UUID ref) {
		FileEl file = dao.findByRef(ref);
				
		Resource resource = diskService.readContentAsResource(ref);
		return Pair.of(file, resource);
	}
	
	@Override
	public Pair<FileEl, Resource> downloadPreview(UUID ref) {
		FileEl file = dao.findByRef(ref);
				
		Resource resource = diskService.readPreviewAsResource(ref);
		return Pair.of(file, resource);
	}

	@Override
	public Resource downloadAsZip(List<UUID> refs) {
		return diskService.createZip(
					refs.stream()
					.map(ref -> dao.findByRef(ref))
					.map(file -> Pair.of(file.getRef(), file.getName()))
					.collect(Collectors.toList())
		);
	}
		
	@Override
	public void copy(FileEl file, FileEl copy) {
		try {
			diskService.copyContent(file.getRef(), copy.getRef());
			if(file.getPreviewMimeType() != null) {
				diskService.copyPreview(file.getRef(), copy.getRef());
			}
		} 
		catch (IOException e) {
			throw new TcmException("Cannot write file on disk.", e);
		}
	}	
	
	// ################################### PRIVATE #########################################################################
	
	private FileEl upload(MultipartFile uploadedFile, UUID dirRef) {
		var file = new FileEl();
		elementLogic.fillNew(file);
		file.setName(uploadedFile.getOriginalFilename());
		file.setMimeType(uploadedFile.getContentType());
		file.setSize((int) uploadedFile.getSize());
		file = save(file);
		
		try {
			diskService.saveContent(uploadedFile, file.getRef());
			if(filePreviewGenerator.isAvailable(uploadedFile.getContentType())) {
				Pair<String, byte[]> preview = filePreviewGenerator.createPreview(uploadedFile);
				file.setPreviewMimeType(preview.getKey());
				file.setPreviewSize(preview.getValue().length);
				diskService.savePreview(preview.getValue(), file.getRef());
			}
		} 
		catch (IOException e) {
			throw new TcmException("Cannot write file on disk.", e);
		}

		containsAssocLogic.create(dirRef, file);
		
		return file;
	}
}
