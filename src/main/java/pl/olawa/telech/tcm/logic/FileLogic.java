package pl.olawa.telech.tcm.logic;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import pl.olawa.telech.tcm.dao.FileDAO;
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
	private ContainsAssocLogic containsAssocLogic;	
	@Autowired
	private ElementLogic elementLogic;
	
	
	public FileLogic(FileDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	public File upload(MultipartFile uploadedFile, UUID dirRef) {
		File file = new File();
		elementLogic.fillNew(file);
		file.setName(uploadedFile.getOriginalFilename());
		file.setMimeType(uploadedFile.getContentType());
		file.setSize((int) uploadedFile.getSize());
		file = save(file);
		
		try {
			diskService.saveFile(uploadedFile, file.getRef());
		} 
		catch (IOException e) {
			throw new TcmException("Cannot write file on disk.", e);
		}

		containsAssocLogic.create(dirRef, file);
		
		return file;
	}
	
	public Pair<File, Resource> download(UUID ref) {
		File file = dao.findByRef(ref);
				
		Resource resource = diskService.readFileAsResource(ref);
		return Pair.of(file, resource);
	}

}
