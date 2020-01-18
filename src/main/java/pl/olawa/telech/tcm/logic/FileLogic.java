package pl.olawa.telech.tcm.logic;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.model.entity.Directory;
import pl.olawa.telech.tcm.model.entity.File;
import pl.olawa.telech.tcm.model.exception.TcmException;
import pl.olawa.telech.tcm.repository.DirectoryRepository;
import pl.olawa.telech.tcm.repository.FileRepository;

@Slf4j
@Service
@Transactional
public class FileLogic extends AbstractLogic<File> {

	@Autowired
	private DiskService diskService;
	
	@Autowired
	private ContainsAssocLogic containsAssocLogic;
	
	@Autowired
	private DirectoryLogic directoryLogic;
	
	
	public FileLogic(FileRepository repository) {
		super(repository);
	}
	
	public void upload(MultipartFile uploadedFile, UUID dirRef) {
		UUID ref = UUID.randomUUID();
		File file = new File();
		file.setName(uploadedFile.getOriginalFilename());
		file.setMimeType(uploadedFile.getContentType());
		file.setRef(ref);
		file.setCreatedBy(accountLogic.getCurrentUser());
		file.setCreatedTime(LocalDateTime.now());
		try {
			diskService.saveFile(uploadedFile, ref);
		} 
		catch (IOException e) {
			throw new TcmException("Cannot write file on disk.", e);
		}
		repository.save(file);
		
		Directory dir = directoryLogic.loadById(dirId);
		
		containsAssocLogic.create(dir, file);
	}
	
	public byte[] download(UUID ref) {
		byte[] bytes;
		try {
			bytes = diskService.readFile(ref);
		} 
		catch (IOException e) {
			throw new TcmException("Cannot read file from disk.", e);
		}
		return bytes;
	}

}
