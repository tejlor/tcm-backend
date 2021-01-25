package pl.olawa.telech.tcm.repo.logic.interfaces;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;

public interface FileLogic {

	FileEl loadByRef(UUID ref);

	List<FileEl> upload(MultipartFile[] uploadedFiles, UUID dirRef);

	Pair<FileEl, Resource> downloadContent(UUID ref);

	Pair<FileEl, Resource> downloadPreview(UUID ref);

	Resource downloadAsZip(List<UUID> refs);

	void copy(FileEl file, FileEl copy);

}
