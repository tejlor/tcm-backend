package pl.olawa.telech.tcm.logic.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.model.exception.TcmException;

@Slf4j
@Service
public class DiskService {
	
	@Value("${tcm.repo.basePath}")
	private String basePath;
	

	public void saveFile(MultipartFile file, UUID ref) throws IOException {
		Path filePath = refToFilePath(ref);
		Files.createDirectories(refToDirPath(ref));
		log.debug("File saved in " + filePath);
		
        try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	public Resource readFileAsResource(UUID ref) {
		try {
			Path path = refToFilePath(ref);
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
	
	private Path refToDirPath(UUID ref) {
		return Paths.get(refToDirStr(ref));		
	}
	
	private Path refToFilePath(UUID ref) {
		return Paths.get(new StringBuilder(refToDirStr(ref))
				.append(ref.toString())
				.append(".bin")
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