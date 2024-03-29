package pl.olawa.telech.tcm.repo.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.builder.SettingBuilder;
import pl.olawa.telech.tcm.adm.builder.UserBuilder;
import pl.olawa.telech.tcm.adm.builder.UserGroupBuilder;
import pl.olawa.telech.tcm.adm.model.entity.Setting;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.adm.model.entity.UserGroup;
import pl.olawa.telech.tcm.commons.model.dto.TableDataDto;
import pl.olawa.telech.tcm.commons.model.dto.TableDataDto.TableInfoDto;
import pl.olawa.telech.tcm.commons.model.dto.TreeNodeDto;
import pl.olawa.telech.tcm.commons.model.shared.Path;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.repo.builder.AccessRightBuilder;
import pl.olawa.telech.tcm.repo.builder.assoc.ContainsAssocBuilder;
import pl.olawa.telech.tcm.repo.builder.element.FileElBuilder;
import pl.olawa.telech.tcm.repo.builder.element.FolderElBuilder;
import pl.olawa.telech.tcm.repo.builder.feature.FeatureAttributeBuilder;
import pl.olawa.telech.tcm.repo.builder.feature.FeatureAttributeValueBuilder;
import pl.olawa.telech.tcm.repo.builder.feature.FeatureBuilder;
import pl.olawa.telech.tcm.repo.logic.FileLogicImpl;
import pl.olawa.telech.tcm.repo.logic.service.DiskServiceImpl;
import pl.olawa.telech.tcm.repo.model.dto.*;
import pl.olawa.telech.tcm.repo.model.entity.AccessRight;
import pl.olawa.telech.tcm.repo.model.entity.assoc.ContainsAssoc;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;
import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;
import pl.olawa.telech.tcm.repo.model.entity.feature.Feature;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttribute;
import pl.olawa.telech.tcm.repo.model.entity.feature.FeatureAttributeValue;
import pl.olawa.telech.tcm.repo.model.enums.FeatureAttributeType;
import pl.olawa.telech.tcm.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class ElementControllerTest extends BaseTest {

	@Autowired
	ElementController elementController;
	@Autowired
	FileLogicImpl fileLogic;	

	@Test
	@Transactional
	public void get_file() {
		// given
		FileEl file = setupFileElement("Document.pdf");
		flush();	
		// when
		FileDto fileDto = (FileDto) elementController.get(file.getRef());
		flushAndClear();
		// then
		assertFileElement(fileDto, file);
	}
	
	@Test
	@Transactional
	public void get_folder() {
		// given
		FolderEl folder = setupFolderElement("Documents");
		flush();	
		// when
		FolderDto folderDto = (FolderDto) elementController.get(folder.getRef());
		flushAndClear();
		// then
		assertFolderElement(folderDto, folder);
	}
	
	@Test
	@Transactional
	public void path() {
		// given
		FolderEl folder = setupFolderElement("Root");
		FileEl file = setupFileElement("Document.pdf");
		setupContainsAssoc(folder, file);
		flush();	
		// when
		Path path = elementController.path(file.getRef());
		flushAndClear();
		// then
		assertThat(path).isNotNull();
		assertThat(path.getNames()).isEqualTo(folder.getName() + "/" + file.getName());
		assertThat(path.getRefs()).isEqualTo(folder.getRef() + "/" + file.getRef());
	}
	
	@Test
	@Transactional
	public void childrenTree() {
		// given
		FolderEl root = setupFolderElement("Root");
		FolderEl folder1 = setupFolderElement("Temp");
		FileEl file1 = setupFileElement("Movie.avi");
		FileEl file2 = setupFileElement("Image.jpg");
		FileEl file3 = setupFileElement("Document.pdf");
		setupContainsAssoc(root, folder1);
		setupContainsAssoc(root, file1);
		setupContainsAssoc(root, file2);
		setupContainsAssoc(root, file3);
		flush();	
		// when
		List<TreeNodeDto> children = elementController.childrenTree(root.getRef());
		flushAndClear();
		// then
		assertThat(children).isNotNull();
		assertThat(children).hasSize(4);
		assertTreeNode(children.get(0), file3);   // sorted
		assertTreeNode(children.get(1), file2);
		assertTreeNode(children.get(2), file1);
		assertTreeNode(children.get(3), folder1);
	}
	
	@Test
	@Transactional
	public void parentsTree() {
		// given
		FolderEl root = setupFolderElement("Root");
		FolderEl folder11 = setupFolderElement("Temp");
		FileEl file11 = setupFileElement("Movie.avi");
		FileEl file21 = setupFileElement("Image.jpg");
		FileEl file22 = setupFileElement("Document.pdf");
		setupContainsAssoc(root, folder11);
		setupContainsAssoc(root, file11);
		setupContainsAssoc(folder11, file21);
		setupContainsAssoc(folder11, file22);
		setupSetting(Setting.ROOT_REF, root.getRef());
		flush();	
		// when
		TreeNodeDto treeNodeDto = elementController.parentsTree(file21.getRef());
		flushAndClear();
		// then
		assertThat(treeNodeDto).isNotNull();
		assertThat(treeNodeDto.getRef()).isEqualTo(root.getRef().toString());
		List<TreeNodeDto> children1 = treeNodeDto.getChildren();
		assertThat(children1).hasSize(2);
		assertThat(children1.get(0).getRef()).isEqualTo(file11.getRef().toString());
		assertThat(children1.get(1).getRef()).isEqualTo(folder11.getRef().toString());
		List<TreeNodeDto> children2 = children1.get(1).getChildren();
		assertThat(children2).hasSize(2);
		assertThat(children2.get(0).getRef()).isEqualTo(file22.getRef().toString());
		assertThat(children2.get(1).getRef()).isEqualTo(file21.getRef().toString());
	}
	
	@Test
	@Transactional
	public void childrenTable() {
		// given
		FolderEl root = setupFolderElement("Root");
		FolderEl folder1 = setupFolderElement("Temp");
		FileEl file1 = setupFileElement("Movie.avi");
		FileEl file2 = setupFileElement("Image.jpg");
		FileEl file3 = setupFileElement("Document.pdf");
		setupContainsAssoc(root, folder1);
		setupContainsAssoc(root, file1);
		setupContainsAssoc(root, file2);
		setupContainsAssoc(root, file3);
		flush();	
		// when
		TableDataDto<ElementDto> tableDataDto = elementController.childrenTable(root.getRef(), null, null, null, null, null);
		flushAndClear();
		// then
		assertThat(tableDataDto).isNotNull();
		
		TableParams params = tableDataDto.getTableParams();
		assertThat(params).isNotNull();
		assertThat(params.getPageNo()).isEqualTo(0);
		assertThat(params.getPageSize()).isEqualTo(10);
		assertThat(params.getFilter()).isNull();
		assertThat(params.getSortBy()).isEqualTo("name");
		assertThat(params.isSortAsc()).isEqualTo(true);
		
		TableInfoDto info = tableDataDto.getTableInfo();
		assertThat(info).isNotNull();
		assertThat(info.getPageCount()).isEqualTo(1);
		assertThat(info.getRowCount()).isEqualTo(4);
		assertThat(info.getRowStart()).isEqualTo(1);
		assertThat(info.getRowEnd()).isEqualTo(4);
		
		List<ElementDto> rows = tableDataDto.getRows();
		assertThat(rows).isNotNull();
		assertThat(rows).hasSize(4);
		assertElement(rows.get(0), file3);
		assertElement(rows.get(1), file2);
		assertElement(rows.get(2), file1);
		assertElement(rows.get(3), folder1);
	}
	
	@Test
	@Transactional
	public void childrenTable_params() {
		// given
		FolderEl root = setupFolderElement("Root");
		FolderEl folder1 = setupFolderElement("Temp");
		FileEl file1 = setupFileElement("Movie.avi");
		FileEl file2 = setupFileElement("Image.jpg");
		FileEl file3 = setupFileElement("Document.pdf");
		setupContainsAssoc(root, folder1);
		setupContainsAssoc(root, file1);
		setupContainsAssoc(root, file2);
		setupContainsAssoc(root, file3);
		flush();	
		// when
		TableDataDto<ElementDto> tableDataDto = elementController.childrenTable(root.getRef(), 0, 5, "doc", "ref", false);
		flushAndClear();
		// then
		assertThat(tableDataDto).isNotNull();
		
		TableParams params = tableDataDto.getTableParams();
		assertThat(params).isNotNull();
		assertThat(params.getPageNo()).isEqualTo(0);
		assertThat(params.getPageSize()).isEqualTo(5);
		assertThat(params.getFilter()).isEqualTo("doc");
		assertThat(params.getSortBy()).isEqualTo("ref");
		assertThat(params.isSortAsc()).isEqualTo(false);
		
		TableInfoDto info = tableDataDto.getTableInfo();
		assertThat(info).isNotNull();
		assertThat(info.getPageCount()).isEqualTo(1);
		assertThat(info.getRowCount()).isEqualTo(1);
		assertThat(info.getRowStart()).isEqualTo(1);
		assertThat(info.getRowEnd()).isEqualTo(1);
		
		List<ElementDto> rows = tableDataDto.getRows();
		assertThat(rows).isNotNull();
		assertThat(rows).hasSize(1);
	}
	
	@Test
	@Transactional
	public void rename() {
		// given
		FileEl file = setupFileElement("Document.pdf");
		flush();	
		// when
		elementController.rename(file.getRef().toString(), "Presentation.pdf");
		flushAndClear();
		// then
		FileEl newFile = load(FileEl.class, file.getId());
		assertThat(newFile).isNotNull();
		assertThat(newFile.getRef()).isEqualTo(file.getRef());
		assertThat(newFile.getName()).isEqualTo("Presentation.pdf");
	}
	
	@Test
	@Transactional
	public void move() {
		// given
		FolderEl oldFolder = setupFolderElement("Old Documents");
		FolderEl newFolder = setupFolderElement("New Documents");
		FileEl file = setupFileElement("Document.pdf");
		setupContainsAssoc(oldFolder, file);
		flush();	
		// when
		elementController.move(Collections.singletonList(file.getRef().toString()), newFolder.getRef().toString());
		flushAndClear(); 
		// then
		FileEl movedFile = load(FileEl.class, file.getId());
		assertThat(movedFile).isNotNull();
		assertThat(movedFile.getRef()).isEqualTo(file.getRef());
		Element parentEl = movedFile.getParentElement();
		assertThat(parentEl).isNotNull();
		assertThat(parentEl.getRef()).isNotEqualTo(oldFolder.getRef());
		assertThat(parentEl.getRef()).isEqualTo(newFolder.getRef());
	}
	
	@Test
	@Transactional
	public void copy_file() {
		// given
		FolderEl oldFolder = setupFolderElement("Old Documents");
		FolderEl newFolder = setupFolderElement("New Documents");
		FileEl file = setupFileElement("Document.pdf");
		setupContainsAssoc(oldFolder, file);
		DiskServiceImpl diskService = mockDiskService();
		flush();	
		// when
		elementController.copy(Collections.singletonList(file.getRef().toString()), newFolder.getRef().toString());
		flushAndClear(); 
		// then
		FileEl oldFile = load(FileEl.class, file.getId());
		assertThat(oldFile).isNotNull();
		assertThat(oldFile.getRef()).isEqualTo(file.getRef());
		Element parentEl = oldFile.getParentElement();
		assertThat(parentEl).isNotNull();
		assertThat(parentEl.getRef()).isEqualTo(oldFolder.getRef());
		
		newFolder = load(FolderEl.class, newFolder.getId());
		ContainsAssoc childAssoc = newFolder.getChildrenAssoc().iterator().next();
		assertThat(childAssoc).isNotNull();
		FileEl copiedFile = (FileEl) childAssoc.getChildElement();
		assertCopiedFileElement(copiedFile, file);	
		try {
			verify(diskService).copyContent(oldFile.getRef(), copiedFile.getRef());
			verify(diskService).copyPreview(oldFile.getRef(), copiedFile.getRef());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Transactional
	public void copy_folder() {
		// given
		FolderEl oldFolder = setupFolderElement("Old Documents");
		FolderEl newFolder = setupFolderElement("New Documents");
		FolderEl folder1 = setupFolderElement("New Documents");
		FileEl file21 = setupFileElement("Document.pdf");
		FileEl file22 = setupFileElement("Image.jpg");
		setupContainsAssoc(oldFolder, folder1);
		setupContainsAssoc(folder1, file21);
		setupContainsAssoc(folder1, file22);
		DiskServiceImpl diskService = mockDiskService();
		flush();	
		// when
		elementController.copy(Collections.singletonList(folder1.getRef().toString()), newFolder.getRef().toString());
		flushAndClear(); 
		// then
		FolderEl oldFolder1 = load(FolderEl.class, folder1.getId());
		assertThat(oldFolder1).isNotNull();
		assertThat(oldFolder1.getRef()).isEqualTo(folder1.getRef());
		Element parentEl = oldFolder1.getParentElement();
		assertThat(parentEl).isNotNull();
		assertThat(parentEl.getRef()).isEqualTo(oldFolder.getRef());
		
		newFolder = load(FolderEl.class, newFolder.getId());
		ContainsAssoc childAssoc = newFolder.getChildrenAssoc().iterator().next();
		assertThat(childAssoc).isNotNull();
		FolderEl copiedFolder1 = (FolderEl) childAssoc.getChildElement();
		assertCopiedFolderElement(copiedFolder1, folder1);
		
		List<Element> children = copiedFolder1.getChildrenAssoc().stream().map(a -> a.getChildElement()).collect(Collectors.toList());
		FileEl copiedFile21 = (FileEl) children.get(0);
		assertCopiedFileElement(copiedFile21, file21);
		FileEl copiedFile22 = (FileEl) children.get(1);
		assertCopiedFileElement(copiedFile22, file22);	
		try {
			verify(diskService).copyContent(file21.getRef(), copiedFile21.getRef());
			verify(diskService).copyPreview(file21.getRef(), copiedFile21.getRef());
			verify(diskService).copyContent(file22.getRef(), copiedFile22.getRef());
			verify(diskService).copyPreview(file22.getRef(), copiedFile22.getRef());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Transactional
	public void remove() {
		// given
		FolderEl folder = setupFolderElement("Documents");
		FolderEl trash = setupFolderElement("Trash");
		FileEl file = setupFileElement("Document.pdf");
		setupContainsAssoc(folder, file);
		setupSetting(Setting.TRASH_REF, trash.getRef());
		flush();	
		// when
		elementController.remove(Collections.singletonList(file.getRef().toString()));
		flushAndClear(); 
		// then
		FileEl deletedFile = load(FileEl.class, file.getId());
		assertThat(deletedFile).isNotNull();
		assertThat(deletedFile.getRef()).isEqualTo(file.getRef());
		Element parentEl = deletedFile.getParentElement();
		assertThat(parentEl).isNotNull();
		assertThat(parentEl.getRef()).isNotEqualTo(folder.getRef());
		assertThat(parentEl.getRef()).isEqualTo(trash.getRef());
	}
	
	@Test
	@Transactional
	public void addFeature() {
		// given
		Element element = setupFileElement("Document.pdf");
		FeatureAttribute numberAttribute = setupFeatureAttribute("Number", FeatureAttributeType.STRING);
		FeatureAttribute amountAttribute = setupFeatureAttribute("Amount", FeatureAttributeType.DEC);
		FeatureAttribute dateAttribute = setupFeatureAttribute("Payment date", FeatureAttributeType.DATE);
		Feature feature = setupFeature("Important invoice", "invoice", numberAttribute, amountAttribute, dateAttribute);
		flush();
		// when
		ElementDto result = elementController.addFeature(element.getRef(), feature.getId());
		// then
		assertThat(result).isNotNull();
		assertThat(result.getRef()).isEqualTo(element.getRef().toString());
		List<FeatureDto> elementFeatures = result.getFeatures(); 
		assertThat(elementFeatures).hasSize(1);
		assertThat(elementFeatures.get(0).getName()).isEqualTo(feature.getName());
	}
	
	@Test
	@Transactional
	public void getFeatureValues() {
		// given
		Element element = setupFileElement("Document.pdf");
		FeatureAttribute numberAttribute = setupFeatureAttribute("Number", FeatureAttributeType.STRING);
		FeatureAttribute amountAttribute = setupFeatureAttribute("Amount", FeatureAttributeType.DEC);
		FeatureAttribute dateAttribute = setupFeatureAttribute("Payment date", FeatureAttributeType.DATE);
		Feature feature = setupFeature("Important invoice", "invoice", numberAttribute, amountAttribute, dateAttribute);
		FeatureAttributeValue numberValue = setupFeatureAttributeValue(element, numberAttribute, "45778/8595/2011");
		FeatureAttributeValue amountValue = setupFeatureAttributeValue(element, amountAttribute, new BigDecimal("1548.48"));
		element.addFeature(feature);
		flush();	
		// when
		List<FeatureAttributeValueDto> result = elementController.getFeatureValues(element.getRef(), feature.getId());
		// then
		assertThat(result).isNotEmpty();
		assertFeatureAttributeValue(result.get(0), numberValue);
		assertFeatureAttributeValue(result.get(1), amountValue);
		assertFeatureAttributeValue(result.get(2), buildFeatureAttributeValue(element, dateAttribute));
	}
	
	@Test
	@Transactional
	public void getAccessRights() {
		// given
		Element element = setupFileElement("Document.pdf");
		User user = setupUser("Jakub", "Walczak", "jwalczak@gmail.com");
		UserGroup userGroup = setupUserGroup("Pracownicy");
		AccessRight accessRight0 = setupAccessRight(element.getId(), 0, Collections.emptySet(), Set.of(user), true, true, true ,true);
		AccessRight accessRight1 = setupAccessRight(element.getId(), 1, Set.of(userGroup), Collections.emptySet(), false, true, false ,false);
		AccessRight accessRight2 = setupAccessRight(element.getId(), 2, Collections.emptySet(), Collections.emptySet(), false, true, false ,false);
		// when
		List<AccessRightDto> result = elementController.getAccessRights(element.getRef());
		//then
		assertThat(result).isNotEmpty();
		assertAccessRight(result.get(0), accessRight0);
		assertAccessRight(result.get(1), accessRight1);
		assertAccessRight(result.get(2), accessRight2);
	}
	
	@Test
	@Transactional
	public void saveAccessRights() {
		// given
		Element element = setupFileElement("Document.pdf");
		User user = setupUser("Jakub", "Walczak", "jwalczak@gmail.com");
		UserGroup userGroup = setupUserGroup("Pracownicy");
		AccessRight oldAccessRight = setupAccessRight(element.getId(), 1, Collections.emptySet(), Set.of(user), true, true, true, false);
		AccessRightDto accessRight0Dto = setupAccessRightDto(0, Set.of(userGroup), Collections.emptySet(), false, true, false ,false);
		AccessRightDto accessRight3Dto = setupAccessRightDto(3, Collections.emptySet(), Collections.emptySet(), false, true, false ,false);
		List<AccessRightDto> accessRights = Arrays.asList(accessRight0Dto, accessRight3Dto);
		// when
		List<AccessRightDto> result = elementController.saveAccessRights(element.getRef(), accessRights);
		flushAndClear();
		// then
		AccessRight loadedOldAccessRight = load(AccessRight.class, oldAccessRight.getId());
		assertThat(loadedOldAccessRight).isNull();
		assertAccessRight(result.get(0), accessRight0Dto);
		assertAccessRight(result.get(1), accessRight3Dto);
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void assertTreeNode(TreeNodeDto nodeDto, Element element){
		assertThat(nodeDto.getRef()).isEqualTo(element.getRef().toString());
		assertThat(nodeDto.getName()).isEqualTo(element.getName());
		assertThat(nodeDto.isLeaf()).isEqualTo(element instanceof FileEl);
		assertThat(nodeDto.getChildren()).isNull();
	}
	
	private void assertFileElement(FileDto fileDto, FileEl file) {
		assertElement(fileDto, file);
		assertThat(fileDto.getTypeName()).isEqualTo("File");	
		assertThat(fileDto.getSize()).isEqualTo(file.getSize());	
		assertThat(fileDto.getMimeType()).isEqualTo(file.getMimeType());
		assertThat(fileDto.getPreviewSize()).isEqualTo(file.getPreviewSize());
		assertThat(fileDto.getPreviewMimeType()).isEqualTo(file.getPreviewMimeType());
	}
	
	private void assertFolderElement(FolderDto folderDto, FolderEl folder) {
		assertElement(folderDto, folder);
		assertThat(folderDto.getTypeName()).isEqualTo("Folder");	
		assertThat(folderDto.getIcon()).isEqualTo(folder.getIcon());	
	}
	
	private void assertElement(ElementDto elementDto, Element element) {
		assertThat(elementDto.getId()).isEqualTo(element.getId());
		assertThat(elementDto.getCreatedByName()).isEqualTo(element.getCreatedBy().calcFirstLastName());
		assertThat(elementDto.getCreatedTime()).isEqualTo(element.getCreatedTime());
		assertThat(elementDto.getModifiedByName()).isEqualTo(element.getModifiedBy().calcFirstLastName());
		assertThat(elementDto.getModifiedTime()).isEqualTo(element.getModifiedTime());
		assertThat(elementDto.getRef()).isEqualTo(element.getRef().toString());
		assertThat(elementDto.getName()).isEqualTo(element.getName());
	}
	
	private void assertCopiedElement(Element copy, Element original) {
		assertThat(copy.getId()).isNotEqualTo(original.getId());
		assertThat(copy.getCreatedTime()).isNotEqualTo(original.getCreatedTime());
		assertThat(copy.getModifiedTime()).isNull();
		assertThat(copy.getRef()).isNotEqualTo(original.getRef().toString());
		assertThat(copy.getName()).isEqualTo(original.getName());
	}
	
	private void assertCopiedFileElement(FileEl copy, FileEl original) {
		assertCopiedElement(copy, original);	
		assertThat(copy.getSize()).isEqualTo(original.getSize());	
		assertThat(copy.getMimeType()).isEqualTo(original.getMimeType());
		assertThat(copy.getPreviewSize()).isEqualTo(original.getPreviewSize());	
		assertThat(copy.getPreviewMimeType()).isEqualTo(original.getPreviewMimeType());
	}
	
	private void assertCopiedFolderElement(FolderEl copy, FolderEl original) {
		assertCopiedElement(copy, original);	
		assertThat(copy.getIcon()).isEqualTo(original.getIcon());	
	}
	
	private void assertFeatureAttributeValue(FeatureAttributeValueDto valueDto, FeatureAttributeValue value) {
		assertThat(valueDto.getId()).isEqualTo(value.getId());
		assertThat(valueDto.getElementId()).isEqualTo(value.getElementId());
		assertThat(valueDto.getAttribute().getId()).isEqualTo(value.getFeatureAttributeId());
		assertThat(valueDto.getAttribute().getName()).isEqualTo(value.getFeatureAttribute().getName());
		assertThat(valueDto.getAttribute().getType()).isEqualTo(value.getFeatureAttribute().getType().name());
		assertThat(valueDto.getValue()).isEqualTo(valueDto.getValue());
	}
	
	private void assertAccessRight(AccessRightDto dto, AccessRight model) {
		assertThat(dto.getId()).isEqualTo(model.getId());
		assertThat(dto.getElementId()).isEqualTo(model.getElementId());
		assertThat(dto.getOrderNo()).isEqualTo(model.getOrderNo());
		assertThat(dto.getUserIds()).containsAll(model.getUsers().stream().map(User::getId).collect(Collectors.toSet()));
		assertThat(dto.getUserGroupIds()).containsAll(model.getUserGroups().stream().map(UserGroup::getId).collect(Collectors.toSet()));
		assertThat(dto.isForAll()).isEqualTo(model.isForAll());
		assertThat(dto.isCanCreate()).isEqualTo(model.isCanCreate());
		assertThat(dto.isCanRead()).isEqualTo(model.isCanRead());
		assertThat(dto.isCanUpdate()).isEqualTo(model.isCanUpdate());
		assertThat(dto.isCanDelete()).isEqualTo(model.isCanDelete());
	}
	
	private void assertAccessRight(AccessRightDto dto, AccessRightDto dto2) {
		assertThat(dto.getUserIds()).containsAll(dto2.getUserIds());
		assertThat(dto.getUserGroupIds()).containsAll(dto2.getUserGroupIds());
		assertThat(dto.isForAll()).isEqualTo(dto2.isForAll());
		assertThat(dto.isCanCreate()).isEqualTo(dto2.isCanCreate());
		assertThat(dto.isCanRead()).isEqualTo(dto2.isCanRead());
		assertThat(dto.isCanUpdate()).isEqualTo(dto2.isCanUpdate());
		assertThat(dto.isCanDelete()).isEqualTo(dto2.isCanDelete());
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
	
	private ContainsAssoc setupContainsAssoc(Element parent, Element child) {
		return new ContainsAssocBuilder()
			.parentElement(parent)
			.childElement(child)
			.saveAndReload(entityManager);
	}
	
	private Feature setupFeature(String name, String code, FeatureAttribute... attributes) {
		return new FeatureBuilder()
				.name(name)
				.code(code)
				.attributes(Arrays.stream(attributes).collect(Collectors.toSet()))
				.saveAndReload(entityManager);
	}
	
	private FeatureAttribute setupFeatureAttribute(String name, FeatureAttributeType type) {
		return new FeatureAttributeBuilder()
				.name(name)
				.type(type)
				.saveAndReload(entityManager);
	}
	
	private FeatureAttributeValue setupFeatureAttributeValue(Element element, FeatureAttribute attribute, Object value) {
		return new FeatureAttributeValueBuilder()
				.element(element)
				.attribute(attribute)
				.value(value)
				.saveAndReload(entityManager);
	}
	
	private FeatureAttributeValue buildFeatureAttributeValue(Element element, FeatureAttribute attribute) {
		return new FeatureAttributeValueBuilder()
				.element(element)
				.attribute(attribute)
				.build();
	}
	
	private Setting setupSetting(String name, Object value) {
		return new SettingBuilder()
			.name(name)
			.value(value.toString())
			.save(entityManager);
	}
	
	private AccessRight setupAccessRight(int elementId, int order, Set<UserGroup> userGroupIds, Set<User> userIds, 
										 boolean create, boolean read, boolean modify, boolean delete) {
		return new AccessRightBuilder()
			.elementId(elementId)
			.userGroups(userGroupIds)
			.users(userIds)
			.orderNo(order)
			.canCreate(create)
			.canRead(read)
			.canUpdate(modify)
			.canDelete(delete)
			.saveAndReload(entityManager);
	}
	
	private AccessRightDto setupAccessRightDto(int order, Set<UserGroup> userGroupIds, Set<User> userIds, 
											   boolean create, boolean read, boolean modify, boolean delete) {
		return new AccessRightDto(new AccessRightBuilder()
			.userGroups(userGroupIds)
			.users(userIds)
			.orderNo(order)
			.canCreate(create)
			.canRead(read)
			.canUpdate(modify)
			.canDelete(delete)
			.build());
	}
	
	private User setupUser(String firstName, String lastName, String email) {
		return new UserBuilder()
			.firstName(firstName)
			.lastName(lastName)
			.email(email)
			.saveAndReload(entityManager);
	}
	
	private UserGroup setupUserGroup(String name) {
		return new UserGroupBuilder()
			.name(name)
			.saveAndReload(entityManager);
	}
	
	@SneakyThrows
	private DiskServiceImpl mockDiskService() {
		var diskService = mock(DiskServiceImpl.class, Mockito.CALLS_REAL_METHODS);
		Mockito.doNothing().when(diskService).copyContent(any(), any());
		Mockito.doNothing().when(diskService).copyPreview(any(), any());
		setBeanField(fileLogic, "diskService", diskService);
		return diskService;
	}
}
