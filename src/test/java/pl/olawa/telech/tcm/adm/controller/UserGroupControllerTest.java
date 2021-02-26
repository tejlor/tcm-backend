package pl.olawa.telech.tcm.adm.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.builder.UserGroupBuilder;
import pl.olawa.telech.tcm.adm.model.dto.UserGroupDto;
import pl.olawa.telech.tcm.adm.model.entity.UserGroup;
import pl.olawa.telech.tcm.commons.model.dto.TableDataDto;
import pl.olawa.telech.tcm.commons.model.dto.TableDataDto.TableInfoDto;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class UserGroupControllerTest extends BaseTest {

	@Autowired
	UserGroupController userGroupController;

	@Test
	@Transactional
	public void getAll() {
		// given
		var userGroupList = new ArrayList<UserGroup>();
		userGroupList.add(setupUserGroup("Administratorzy"));
		userGroupList.add(setupUserGroup("Sprzedawcy"));
		userGroupList.add(setupUserGroup("Kadry"));
		int count = userGroupList.size();
		flush();	
		// when
		List<UserGroupDto> result = userGroupController.getAll();	
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		assertThat(result.size()).isEqualTo(count);
		for(int i = 0; i < count; i++) {
			assertUserGroup(result.get(i), userGroupList.get(i));
		}
	}
	
	@Test
	@Transactional
	public void getTable() {
		// given
		var userGroupList = new ArrayList<UserGroup>();
		userGroupList.add(setupUserGroup("Administratorzy"));
		userGroupList.add(setupUserGroup("Sprzedawcy"));
		userGroupList.add(setupUserGroup("Kadry"));
		userGroupList.add(setupUserGroup("Pracownicy"));
		flush();	
		// when
		TableDataDto<UserGroupDto> result = userGroupController.getTable(0, 2, null, "name", false);	
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		
		TableParams params = result.getTableParams();
		assertThat(params).isNotNull();
		assertThat(params.getPageNo()).isEqualTo(0);
		assertThat(params.getPageSize()).isEqualTo(2);
		assertThat(params.getFilter()).isNull();
		assertThat(params.getSortBy()).isEqualTo("name");
		assertThat(params.isSortAsc()).isEqualTo(false);
		
		TableInfoDto info = result.getTableInfo();
		assertThat(info).isNotNull();
		assertThat(info.getPageCount()).isEqualTo(2);
		assertThat(info.getRowCount()).isEqualTo(4);
		assertThat(info.getRowStart()).isEqualTo(1);
		assertThat(info.getRowEnd()).isEqualTo(2);
		
		assertUserGroup(result.getRows().get(0), userGroupList.get(1)); // Sprzedawcy
		assertUserGroup(result.getRows().get(1), userGroupList.get(3)); // Pracownicy
	}
	
	@Test
	@Transactional
	public void create() {
		// given
		UserGroupDto userGroupDto = setupUserGroupDto("Administratorzy");
		// when
		UserGroupDto result = userGroupController.create(userGroupDto);	
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		
		UserGroup createdUserGroup = load(UserGroup.class, result.getId());
		assertThat(createdUserGroup.getName()).isEqualTo(userGroupDto.getName());
		assertThat(createdUserGroup.getCreatedById()).isNotNull();
		assertThat(createdUserGroup.getCreatedTime()).isNotNull();
	}
	
	@Test
	@Transactional
	public void update() {
		// given
		UserGroup userGroup = setupUserGroup("Administratorzy");
		UserGroupDto userGroupDto = new UserGroupDto(userGroup);
		userGroupDto.setName("Admini");
		// when
		UserGroupDto result = userGroupController.update(userGroupDto.getId(), userGroupDto);	
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		
		UserGroup updatedUserGroup = load(UserGroup.class, result.getId());
		assertThat(updatedUserGroup.getName()).isEqualTo(userGroupDto.getName());
		assertThat(updatedUserGroup.getModifiedById()).isNotNull();
		assertThat(updatedUserGroup.getModifiedTime()).isNotNull();
	}
	
	@Test
	@Transactional
	public void exportToXlsx() {
		// when
		ResponseEntity<ByteArrayResource> result = userGroupController.exportToXlsx();
		// then
		assertThat(result).isNotNull();
		assertThat(result.getBody().contentLength()).isGreaterThan(0);
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void assertUserGroup(UserGroupDto userGroupDto, UserGroup userGroup) {
		assertThat(userGroupDto.getId()).isEqualTo(userGroup.getId());
		assertThat(userGroupDto.getName()).isEqualTo(userGroup.getName());
		assertThat(userGroupDto.getCreatedTime()).isEqualTo(userGroup.getCreatedTime());
		assertThat(userGroupDto.getCreatedByName()).isEqualTo(userGroup.getCreatedBy().calcFirstLastName());
		assertThat(userGroupDto.getModifiedTime()).isEqualTo(userGroup.getModifiedTime());
		assertThat(userGroupDto.getModifiedByName()).isEqualTo(userGroup.getModifiedBy().calcFirstLastName());
	}
	
	private UserGroup setupUserGroup(String name) {
		return new UserGroupBuilder()
			.name(name)
			.saveAndReload(entityManager);
	}
	
	private UserGroupDto setupUserGroupDto(String name) {
		return new UserGroupDto(
			new UserGroupBuilder()
				.name(name)
				.build()
			);
	}
}
