package pl.olawa.telech.tcm.logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DiskService {
	
	@Value("${tcm.repo.basePath}")
	private String basePath;

	public void saveFile(MultipartFile file, UUID ref) throws IOException {
		Path path = refToPath(ref);
		byte[] bytes = file.getBytes();
		Files.createDirectories(path);
        Files.write(path, bytes);
	}
	
	public byte[] readFile(UUID ref) throws IOException {
		Path path = refToPath(ref);
		return Files.readAllBytes(path);
	}
	
	private Path refToPath(UUID ref) {
		return Paths.get(new StringBuilder(ref.toString())
				.insert(4, File.separator)
				.replace(8, 9, File.separator)
				.replace(12, 13, File.separator)
				.replace(16, 28, File.separator)
				.insert(0, basePath)
				.append(ref.toString())
				.toString());		
	}
}
