package pl.olawa.telech.tcm.repo.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.repo.builder.element.FolderElBuilder;
import pl.olawa.telech.tcm.repo.model.dto.ElementDto;
import pl.olawa.telech.tcm.repo.model.dto.FolderDto;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;
import pl.olawa.telech.tcm.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class FolderControllerTest extends BaseTest {

	@Autowired
	FolderController folderController;	

	@Test
	@Transactional
	public void get() {
		// given
		FolderEl folder = setupFolderElement("Documents");
		flush();	
		// when
		FolderDto folderDto = folderController.get(folder.getRef().toString());
		flushAndClear();
		// then
		assertFolderElement(folderDto, folder);
	}
	
	@Test
	@Transactional
	public void create() {
		// given
		FolderEl root = setupFolderElement("Root");
		flush();	
		// when
		var folderDto = new FolderDto();
		folderDto.setName("Documents");
		folderDto.setParentRef(root.getRef().toString());
		FolderDto createdFolderDto = folderController.create(folderDto);
		flushAndClear();
		// then
		assertThat(createdFolderDto.getName()).isEqualTo(folderDto.getName());
		
		FolderEl createdFolder = load(FolderEl.class, createdFolderDto.getId());
		assertFolderElement(createdFolderDto, createdFolder);
		Element parent = createdFolder.getParentElement();
		assertThat(parent.getRef()).isEqualTo(root.getRef());
	}
	
	// ################################### PRIVATE #########################################################################
		
	private void assertFolderElement(FolderDto folderDto, FolderEl folder) {
		assertElement(folderDto, folder);
		assertThat(folderDto.getTypeName()).isEqualTo("Folder");	
		assertThat(folderDto.getIcon()).isEqualTo(folder.getIcon());	
	}
	
	private void assertElement(ElementDto elementDto, Element element) {
		assertThat(elementDto.getId()).isEqualTo(element.getId());
		assertThat(elementDto.getCreatedByName()).isEqualTo(element.getCreatedBy().calcFirstLastName());
		assertThat(elementDto.getCreatedTime()).isEqualTo(element.getCreatedTime());
		assertThat(elementDto.getRef()).isEqualTo(element.getRef().toString());
		assertThat(elementDto.getName()).isEqualTo(element.getName());
	}
	
	private FolderEl setupFolderElement(String name) {
		return new FolderElBuilder()
			.name(name)
			.saveAndReload(entityManager);
	}
}
