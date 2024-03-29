package pl.olawa.telech.tcm.repo.logic.service;

import static lombok.AccessLevel.PRIVATE;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.commons.model.exception.NotFoundException;
import pl.olawa.telech.tcm.commons.model.exception.TcmException;
import pl.olawa.telech.tcm.repo.logic.service.interfaces.DiskService;

@Slf4j
@Service
@FieldDefaults(level = PRIVATE)
public class DiskServiceImpl implements DiskService {
	
	@Value("${tcm.repo.basePath}")
	String basePath;

	private static final String contentExtension = ".bin";
	private static final String previewExtension = ".prv";
	
	@Override
	public void saveContent(MultipartFile file, UUID ref) throws IOException {
		Path path = refToContentPath(ref);
		Files.createDirectories(refToDirPath(ref));
		log.debug("File saved in " + path);
		
        try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	@Override
	public void savePreview(byte[] bytes, UUID ref) throws IOException {		
		Path path = refToPreviewPath(ref);
		log.debug("File preview saved in " + path);
		
        Files.write(path, bytes, StandardOpenOption.CREATE);
	}
	
	@Override
	public void copyContent(UUID originalRef, UUID copyRef) throws IOException {
		Path originalPath = refToContentPath(originalRef);
		
		Path copyPath = refToContentPath(copyRef);
		Files.createDirectories(refToDirPath(copyRef));
		log.debug("File copied to " + copyPath);
		
		Files.copy(originalPath, copyPath);  
	}
	
	@Override
	public void copyPreview(UUID originalRef, UUID copyRef) throws IOException {
		Path originalPath = refToPreviewPath(originalRef);
		
		Path copyPath = refToPreviewPath(copyRef);
		Files.createDirectories(refToDirPath(copyRef));
		log.debug("File preview copied to " + copyPath);
		
		Files.copy(originalPath, copyPath);  
	}
	
	@Override
	public Resource readContentAsResource(UUID ref) {
		try {
			Path path = refToContentPath(ref);
			Resource resource = new UrlResource(path.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new TcmException("Could not read file: " + ref);
			}
		}
		catch (MalformedURLException e) {
			throw new TcmException("Could not read file: " + ref, e);
		}
	}
	
	@Override
	public Resource readPreviewAsResource(UUID ref) {
		try {
			Path path = refToPreviewPath(ref);
			Resource resource = new UrlResource(path.toUri());
			if (!resource.exists())
				throw new NotFoundException();
			
			if(!resource.isReadable())
				throw new TcmException("Could not read file: " + ref);
			
			return resource;
		}
		catch (MalformedURLException e) {
			throw new TcmException("Could not read file: " + ref, e);
		}
	}
	
	@Override
	public File readContentAsFile(UUID ref) {
		Path path = refToContentPath(ref);
		File file = path.toFile();
		if (!file.exists())
			throw new NotFoundException();
		
		if(!file.canRead())
			throw new TcmException("Could not read file: " + ref);
		
		return file;
	}
	
	@Override
	public Resource createZip(List<Pair<UUID, String>> refsWithNames) {
		try {
			Path zipPath = Files.createTempFile(null, null);
			FileOutputStream fos = new FileOutputStream(zipPath.toFile());
		    ZipOutputStream zipos = new ZipOutputStream(fos);
	
			for(Pair<UUID, String> refWithName : refsWithNames) {	
				UUID ref = refWithName.getKey();
				String fileName = refWithName.getValue();
				
				FileInputStream fis = new FileInputStream(readContentAsFile(ref));
		        
				ZipEntry zipEntry = new ZipEntry(fileName);
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
	
	private Path refToDirPath(UUID ref) {
		return Paths.get(refToDirStr(ref));		
	}
	
	private Path refToContentPath(UUID ref) {
		return Paths.get(new StringBuilder(refToDirStr(ref))
				.append(ref.toString())
				.append(contentExtension)
				.toString());		
	}
	
	private Path refToPreviewPath(UUID ref) {
		return Paths.get(new StringBuilder(refToDirStr(ref))
				.append(ref.toString())
				.append(previewExtension)
				.toString());		
	}
	
	/*
	 * 123e4567-e89b-12d3-a456-426655440000
	 * is saved in
	 * basePath/123e/4567/e89b/12d3/a456/123e4567-e89b-12d3-a456-426655440000.bin
	 */
	private String refToDirStr(UUID ref) {
		return new StringBuilder(ref.toString())
				.insert(4, File.separator)
				.replace(9, 10, File.separator)
				.replace(14, 15, File.separator)
				.replace(19, 20, File.separator)
				.replace(24, 37, File.separator)
				.insert(0, basePath)
				.toString();		
	}
}
