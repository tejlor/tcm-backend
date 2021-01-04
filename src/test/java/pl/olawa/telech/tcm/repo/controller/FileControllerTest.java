package pl.olawa.telech.tcm.repo.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.builder.SettingBuilder;
import pl.olawa.telech.tcm.adm.model.entity.Setting;
import pl.olawa.telech.tcm.commons.model.dto.TableDataDto;
import pl.olawa.telech.tcm.commons.model.dto.TableDataDto.TableInfoDto;
import pl.olawa.telech.tcm.commons.model.dto.TreeNodeDto;
import pl.olawa.telech.tcm.commons.model.shared.Path;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.repo.builder.assoc.ContainsAssocBuilder;
import pl.olawa.telech.tcm.repo.builder.element.FileElBuilder;
import pl.olawa.telech.tcm.repo.builder.element.FolderElBuilder;
import pl.olawa.telech.tcm.repo.logic.FileLogic;
import pl.olawa.telech.tcm.repo.logic.service.DiskService;
import pl.olawa.telech.tcm.repo.model.dto.ElementDto;
import pl.olawa.telech.tcm.repo.model.dto.FileDto;
import pl.olawa.telech.tcm.repo.model.dto.FolderDto;
import pl.olawa.telech.tcm.repo.model.entity.assoc.ContainsAssoc;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;
import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;
import pl.olawa.telech.tcm.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class FileControllerTest extends BaseTest {

	@Autowired
	FileController fileController;
	@Autowired
	FileLogic fileLogic;	
	@Captor
	ArgumentCaptor<List<Pair<UUID,String>>> refsWithNamesCaptor;

	@Test
	@Transactional
	public void get() {
		// given
		FileEl file = setupFileElement("Document.pdf");
		flush();	
		// when
		FileDto fileDto = fileController.get(file.getRef().toString());
		flushAndClear();
		// then
		assertFileElement(fileDto, file);
	}
	
	@Test
	@Transactional
	public void preview() {
		// given
		FileEl file = setupFileElement("Document.pdf");
		DiskService diskService = mockDiskService();
		flush();	
		// when
		ResponseEntity<Resource> result = fileController.preview(file.getRef().toString());
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		assertThat(result.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0)).contains(file.getName());
		assertThat(result.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0)).contains(file.getPreviewMimeType());		
		verify(diskService).readPreviewAsResource(file.getRef());
	}
	
	@Test
	@Transactional
	public void content() {
		// given
		FileEl file = setupFileElement("Document.pdf");
		DiskService diskService = mockDiskService();
		flush();	
		// when
		ResponseEntity<Resource> result =  fileController.content(file.getRef().toString());
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		assertThat(result.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0)).contains(file.getName());
		assertThat(result.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0)).contains(file.getMimeType());	
		verify(diskService).readContentAsResource(file.getRef());
	}

	@Test
	@Transactional
	public void downloadAsZip() {
		// given
		FileEl file1 = setupFileElement("Document.pdf");
		FileEl file2 = setupFileElement("Image.jpg");
		DiskService diskService = mockDiskService();
		flush();	
		// when
		List<String> refs = new ArrayList<>();
		refs.add(file1.getRef().toString());
		refs.add(file2.getRef().toString());
		ResponseEntity<Resource> result =  fileController.downloadAsZip(refs);
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		assertThat(result.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0)).contains("TCM_Files.zip");
		assertThat(result.getHeaders().get(HttpHeaders.CONTENT_TYPE).get(0)).contains("application/zip");		
		verify(diskService).createZip(refsWithNamesCaptor.capture());
		List<Pair<UUID, String>> refsWithNames = refsWithNamesCaptor.getValue();
		assertThat(refsWithNames).hasSize(refs.size());
		assertThat(refsWithNames.get(0)).isEqualByComparingTo(Pair.of(file1.getRef(), file1.getName()));
		assertThat(refsWithNames.get(1)).isEqualByComparingTo(Pair.of(file2.getRef(), file2.getName()));
	}

	@Test
	@Transactional
	public void upload() {
		// given
		FolderEl folder = setupFolderElement("Documents");
		flush();	
		// when
		MultipartFile[] multipartFiles = new MultipartFile[] {
				setupMultipartFile("document.pdf", "application/pdf"), 
				setupMultipartFile("DSC001.jpg", "image/jpg")
		};
		List<FileDto> fileDtos =  fileController.upload(multipartFiles, folder.getRef().toString());
		flushAndClear();
		// then
		assertThat(fileDtos).hasSize(2);
		for(int i = 0; i < fileDtos.size(); i++) {
			assertFileElement(fileDtos.get(i), multipartFiles[i], folder.getRef());		
		}
	}

	// ################################### PRIVATE #########################################################################
		
	private void assertFileElement(FileDto fileDto, FileEl file) {
		assertElement(fileDto, file);
		assertThat(fileDto.getTypeName()).isEqualTo("File");	
		assertThat(fileDto.getSize()).isEqualTo(file.getSize());	
		assertThat(fileDto.getMimeType()).isEqualTo(file.getMimeType());
		assertThat(fileDto.getPreviewMimeType()).isEqualTo(file.getPreviewMimeType());
	}
	
	private void assertElement(ElementDto elementDto, Element element) {
		assertThat(elementDto.getId()).isEqualTo(element.getId());
		assertThat(elementDto.getCreatedByName()).isEqualTo(element.getCreatedBy().calcFirstLastName());
		assertThat(elementDto.getCreatedTime()).isEqualTo(element.getCreatedTime());
		assertThat(elementDto.getModifiedByName()).isEqualTo(element.getModifiedBy() != null ? element.getModifiedBy().calcFirstLastName() : null);
		assertThat(elementDto.getModifiedTime()).isEqualTo(element.getModifiedTime());
		assertThat(elementDto.getRef()).isEqualTo(element.getRef().toString());
		assertThat(elementDto.getName()).isEqualTo(element.getName());
	}
	
	private void assertFileElement(FileDto fileDto, MultipartFile multipartFile, UUID parentRef) {
		assertThat(fileDto.getName()).isEqualTo(multipartFile.getOriginalFilename());
		assertThat(fileDto.getMimeType()).isEqualTo(multipartFile.getContentType());
		assertThat(fileDto.getSize()).isEqualTo(multipartFile.getSize());
		
		FileEl createdFile = load(FileEl.class, fileDto.getId());
		assertFileElement(fileDto, createdFile);
		Element parent = createdFile.getParentElement();
		assertThat(parent.getRef()).isEqualTo(parentRef);
	}

	private FileEl setupFileElement(String name) {
		return new FileElBuilder()
			.name(name)
			.saveAndReload(entityManager);
	}
	
	private FolderEl setupFolderElement(String name) {
		return new FolderElBuilder()
			.name(name)
			.saveAndReload(entityManager);
	}
	
	private MultipartFile setupMultipartFile(String name, String contentType) {
		return new MockMultipartFile("name", name, contentType, new byte[100]);
	}
	
	@SneakyThrows
	private DiskService mockDiskService() {
		var diskService = mock(DiskService.class);
		when(diskService.createZip(any())).thenReturn(null);
		Mockito.doNothing().when(diskService).saveContent(any(), any());
		setBeanField(fileLogic, "diskService", diskService);
		return diskService;
	}
}
