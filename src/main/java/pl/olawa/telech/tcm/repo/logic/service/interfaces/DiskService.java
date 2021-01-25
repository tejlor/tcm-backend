package pl.olawa.telech.tcm.repo.logic.service.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface DiskService {

	void saveContent(MultipartFile file, UUID ref) throws IOException;

	void savePreview(byte[] bytes, UUID ref) throws IOException;

	void copyContent(UUID originalRef, UUID copyRef) throws IOException;

	void copyPreview(UUID originalRef, UUID copyRef) throws IOException;

	Resource readContentAsResource(UUID ref);

	Resource readPreviewAsResource(UUID ref);

	File readContentAsFile(UUID ref);

	Resource createZip(List<Pair<UUID, String>> refsWithNames);

}
