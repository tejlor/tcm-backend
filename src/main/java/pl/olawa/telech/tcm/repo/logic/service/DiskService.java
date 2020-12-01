package pl.olawa.telech.tcm.repo.logic.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.commons.model.exception.NotFoundException;
import pl.olawa.telech.tcm.commons.model.exception.TcmException;

@Slf4j
@Service
public class DiskService {
	
	@Value("${tcm.repo.basePath}")
	private String basePath;

	private static final String contentExtension = ".bin";
	private static final String previewExtension = ".prv";
	

	public void saveContent(MultipartFile file, UUID ref) throws IOException {
		Path path = refToContentPath(ref);
		Files.createDirectories(refToDirPath(ref));
		log.debug("File saved in " + path);
		
        try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	public void savePreview(byte[] bytes, UUID ref) throws IOException {		
		Path path = refToPreviewPath(ref);
		log.debug("File preview saved in " + path);
		
        Files.write(path, bytes, StandardOpenOption.CREATE);
	}
	
	public void copyContent(UUID originalRef, UUID copyRef) throws IOException {
		Path originalPath = refToContentPath(originalRef);
		
		Path copyPath = refToContentPath(copyRef);
		Files.createDirectories(refToDirPath(copyRef));
		log.debug("File copied to " + copyPath);
		
		Files.copy(originalPath, copyPath);  
	}
	
	public void copyPreview(UUID originalRef, UUID copyRef) throws IOException {
		Path originalPath = refToPreviewPath(originalRef);
		
		Path copyPath = refToPreviewPath(copyRef);
		Files.createDirectories(refToDirPath(copyRef));
		log.debug("File preview copied to " + copyPath);
		
		Files.copy(originalPath, copyPath);  
	}
	
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
	
	public File readContentAsFile(UUID ref) {
		Path path = refToContentPath(ref);
		File file = path.toFile();
		if (!file.exists())
			throw new NotFoundException();
		
		if(!file.canRead())
			throw new TcmException("Could not read file: " + ref);
		
		return file;
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
