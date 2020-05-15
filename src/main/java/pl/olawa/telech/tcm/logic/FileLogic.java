package pl.olawa.telech.tcm.logic;

import java.io.IOException;
import java.nio.file.Path;
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

import pl.olawa.telech.tcm.dao.FileDAO;
import pl.olawa.telech.tcm.logic.helper.FilePreviewGenerator;
import pl.olawa.telech.tcm.logic.service.DiskService;
import pl.olawa.telech.tcm.model.entity.element.File;
import pl.olawa.telech.tcm.model.exception.TcmException;


@Service
@Transactional
public class FileLogic extends AbstractLogic<File> {

	private FileDAO dao;
	
	@Autowired
	private DiskService diskService;
	@Autowired
	private FilePreviewGenerator filePreviewGenerator;
	
	@Autowired
	private ContainsAssocLogic containsAssocLogic;	
	@Autowired
	private ElementLogic elementLogic;	
	
	
	public FileLogic(FileDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	public File loadByRef(UUID ref) {
		return dao.findByRef(ref);
	}
	
	public List<File> upload(MultipartFile[] uploadedFiles, UUID dirRef) {
		return Arrays.stream(uploadedFiles)
				.map(part -> upload(part, dirRef))
				.collect(Collectors.toList());
	}
	
	private File upload(MultipartFile uploadedFile, UUID dirRef) {
		File file = new File();
		elementLogic.fillNew(file);
		file.setName(uploadedFile.getOriginalFilename());
		file.setMimeType(uploadedFile.getContentType());
		file.setSize((int) uploadedFile.getSize());
		file = save(file);
		
		try {
			diskService.saveContent(uploadedFile, file.getRef());
			if(filePreviewGenerator.isAvailable(uploadedFile.getContentType())) {
				byte[] previewBytes = filePreviewGenerator.createPreview(uploadedFile);
				diskService.savePreview(previewBytes, file.getRef());
			}
		} 
		catch (IOException e) {
			throw new TcmException("Cannot write file on disk.", e);
		}
		
		

		containsAssocLogic.create(dirRef, file);
		
		return file;
	}
	
	public Pair<File, Resource> downloadContent(UUID ref) {
		File file = dao.findByRef(ref);
				
		Resource resource = diskService.readContentAsResource(ref);
		return Pair.of(file, resource);
	}
	
	public Pair<File, Resource> downloadPreview(UUID ref) {
		File file = dao.findByRef(ref);
				
		Resource resource = diskService.readPreviewAsResource(ref);
		return Pair.of(file, resource);
	}

}
