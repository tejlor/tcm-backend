package pl.olawa.telech.tcm.logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import pl.olawa.telech.tcm.dao.FileDAO;
import pl.olawa.telech.tcm.logic.helper.FilePreviewGenerator;
import pl.olawa.telech.tcm.logic.service.DiskService;
import pl.olawa.telech.tcm.model.entity.Setting;
import pl.olawa.telech.tcm.model.entity.element.Element;
import pl.olawa.telech.tcm.model.entity.element.FileEl;
import pl.olawa.telech.tcm.model.exception.TcmException;


@Service
@Transactional
public class FileLogic extends AbstractLogic<FileEl> {

	private FileDAO dao;
	
	@Autowired
	private DiskService diskService;
	@Autowired
	private FilePreviewGenerator filePreviewGenerator;
	
	@Autowired
	private ContainsAssocLogic containsAssocLogic;	
	@Autowired
	private ElementLogic elementLogic;	
	@Autowired
	private SettingLogic settingLogic;
	
	
	public FileLogic(FileDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	public FileEl loadByRef(UUID ref) {
		return dao.findByRef(ref);
	}
	
	public List<FileEl> upload(MultipartFile[] uploadedFiles, UUID dirRef) {
		return Arrays.stream(uploadedFiles)
				.map(part -> upload(part, dirRef))
				.collect(Collectors.toList());
	}
	
	private FileEl upload(MultipartFile uploadedFile, UUID dirRef) {
		FileEl file = new FileEl();
		elementLogic.fillNew(file);
		file.setName(uploadedFile.getOriginalFilename());
		file.setMimeType(uploadedFile.getContentType());
		file.setSize((int) uploadedFile.getSize());
		file = save(file);
		
		try {
			diskService.saveContent(uploadedFile, file.getRef());
			if(filePreviewGenerator.isAvailable(uploadedFile.getContentType())) {
				Pair<String, byte[]>preview = filePreviewGenerator.createPreview(uploadedFile);
				file.setPreviewMimeType(preview.getKey());
				diskService.savePreview(preview.getValue(), file.getRef());
			}
		} 
		catch (IOException e) {
			throw new TcmException("Cannot write file on disk.", e);
		}

		containsAssocLogic.create(dirRef, file);
		
		return file;
	}
	
	public Pair<FileEl, Resource> downloadContent(UUID ref) {
		FileEl file = dao.findByRef(ref);
				
		Resource resource = diskService.readContentAsResource(ref);
		return Pair.of(file, resource);
	}
	
	public Pair<FileEl, Resource> downloadPreview(UUID ref) {
		FileEl file = dao.findByRef(ref);
				
		Resource resource = diskService.readPreviewAsResource(ref);
		return Pair.of(file, resource);
	}

	public Resource downloadAsZip(List<UUID> refs) {
		try {
			Path zipPath = Files.createTempFile(null, null);
			FileOutputStream fos = new FileOutputStream(zipPath.toFile());
		    ZipOutputStream zipos = new ZipOutputStream(fos);
	
			for(UUID ref : refs) {
				FileEl file = dao.findByRef(ref);		
				FileInputStream fis = new FileInputStream(diskService.readContentAsFile(ref));
		        
				ZipEntry zipEntry = new ZipEntry(file.getName());
		        zipos.putNextEntry(zipEntry);	 
	            byte[] bytes = new byte[1024];
	            int length;
	            while((length = fis.read(bytes)) >= 0) {
	                zipos.write(bytes, 0, length);
	            }
	            fis.close();
				
			}
			
			zipos.close();
		    fos.close();
		    
			return new UrlResource(zipPath.toUri());
		}
		catch(IOException e) {
			throw new TcmException("Coud not create zip archive.", e);
		}
	}
		
	public void copy(FileEl file) {
		try {
			diskService.copyContent(file.getRef(), file.getRef());
			if(file.getPreviewMimeType() != null) {
				diskService.copyPreview(file.getRef(), file.getRef());
			}
		} 
		catch (IOException e) {
			throw new TcmException("Cannot write file on disk.", e);
		}
	}
	
}
