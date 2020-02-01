package pl.olawa.telech.tcm.logic;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.model.entity.element.Directory;
import pl.olawa.telech.tcm.model.entity.element.File;
import pl.olawa.telech.tcm.model.exception.TcmException;
import pl.olawa.telech.tcm.repository.FileRepository;
import pl.olawa.telech.tcm.utils.TUtils;

@Slf4j
@Service
@Transactional
public class FileLogic extends AbstractLogic<File> {

	private FileRepository repository;
	
	@Autowired
	private DiskService diskService;
	
	@Autowired
	private ContainsAssocLogic containsAssocLogic;
	
	@Autowired
	private DirectoryLogic directoryLogic;
	
	
	public FileLogic(FileRepository repository) {
		super(repository);
		this.repository = repository;
	}
	
	public void upload(MultipartFile uploadedFile, UUID dirRef) {
		File file = File.create();
		file.setName(uploadedFile.getOriginalFilename());
		file.setMimeType(uploadedFile.getContentType());
		try {
			diskService.saveFile(uploadedFile, file.getRef());
		} 
		catch (IOException e) {
			log.debug(e.getMessage());
			throw new TcmException("Cannot write file on disk.", e);
		}
		repository.save(file);
		
		Directory dir = directoryLogic.loadByRef(dirRef);	
		containsAssocLogic.create(dir, file);
	}
	
	public Resource download(UUID ref) {
		File file = repository.findByRef(ref);
		TUtils.assertEntityExists(file);
				
		Resource resource = diskService.readFileAsResource(ref);
		return resource;
	}

}