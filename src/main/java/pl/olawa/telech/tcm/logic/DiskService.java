package pl.olawa.telech.tcm.logic;

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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.model.exception.TcmException;

@Slf4j
@Service
public class DiskService {
	
	@Value("${tcm.repo.basePath}")
	private String basePath;

	public void saveFile(MultipartFile file, UUID ref) throws IOException {
		log.debug(ref.toString());
		Path path = refToPath(ref);
		log.debug(path.toString());
		byte[] bytes = file.getBytes();
		Files.createDirectories(path);
        Files.write(path, bytes);
	}
	
	public byte[] readFile(UUID ref) throws IOException {
		Path path = refToPath(ref);
		return Files.readAllBytes(path);
	}
	
	public Resource readFileAsResource(UUID ref) {
		try {
			Path path = refToPath(ref);
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
	
	private Path refToPath(UUID ref) {
		return Paths.get(new StringBuilder(ref.toString())
				.insert(4, File.separator)
				.replace(9, 10, File.separator)
				.replace(13, 14, File.separator)
				.replace(18, 28, File.separator)
				.insert(0, basePath)
				.append(ref.toString())
				.toString());		
	}
}
