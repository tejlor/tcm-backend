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
import pl.olawa.telech.tcm.adm.builder.UserBuilder;
import pl.olawa.telech.tcm.adm.model.dto.UserDto;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.commons.model.dto.TableDataDto;
import pl.olawa.telech.tcm.commons.model.dto.TableDataDto.TableInfoDto;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class UserControllerTest extends BaseTest {

	@Autowired
	UserController userController;

	@Test
	@Transactional
	public void getAll() {
		// given
		var userList = new ArrayList<User>();
		userList.add(setupUser("Jakub", "Walczak", "jwalczak@gmail.com"));
		userList.add(setupUser("Paulina", "Wach", "pwach@gmail.com"));
		userList.add(setupUser("Bolesław", "Kowalski", "bkowalski@gmail.com"));
		int count = userList.size();
		flush();	
		// when
		List<UserDto> result = userController.getAll();	
		flushAndClear();
		result.remove(0); // delete default user created in base class
		// then
		assertThat(result).isNotNull();
		assertThat(result.size()).isEqualTo(count);
		for(int i = 0; i < count; i++) {
			assertUser(result.get(i), userList.get(i));
		}
	}
	
	@Test
	@Transactional
	public void getTable() {
		// given
		var userList = new ArrayList<User>();
		userList.add(setupUser("Jakub", "Walczak", "jwalczak@gmail.com"));
		userList.add(setupUser("Paulina", "Wach", "pwach@gmail.com"));
		userList.add(setupUser("Bolesław", "Kowalski", "bkowalski@gmail.com"));
		flush();	
		// when
		TableDataDto<UserDto> result = userController.getTable(0, 2, null, "lastName", false);	
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		
		TableParams params = result.getTableParams();
		assertThat(params).isNotNull();
		assertThat(params.getPageNo()).isEqualTo(0);
		assertThat(params.getPageSize()).isEqualTo(2);
		assertThat(params.getFilter()).isNull();
		assertThat(params.getSortBy()).isEqualTo("lastName");
		assertThat(params.isSortAsc()).isEqualTo(false);
		
		TableInfoDto info = result.getTableInfo();
		assertThat(info).isNotNull();
		assertThat(info.getPageCount()).isEqualTo(2);
		assertThat(info.getRowCount()).isEqualTo(4);
		assertThat(info.getRowStart()).isEqualTo(1);
		assertThat(info.getRowEnd()).isEqualTo(2);
		
		assertUser(result.getRows().get(0), userList.get(0)); // Walczak
		assertUser(result.getRows().get(1), userList.get(1)); // Wach
	}
	
	@Test
	@Transactional
	public void create() {
		// given
		UserDto userDto = setupUserDto("Jakub", "Walczak", "jwalczak@bms.com.pl");
		// when
		UserDto result = userController.create(userDto);	
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		
		User createdUser = load(User.class, result.getId());
		assertThat(createdUser.getFirstName()).isEqualTo(userDto.getFirstName());
		assertThat(createdUser.getLastName()).isEqualTo(userDto.getLastName());
		assertThat(createdUser.getEmail()).isEqualTo(userDto.getEmail());
		assertThat(createdUser.getPassword()).isEqualTo(userDto.getEmail());
		assertThat(createdUser.getCreatedById()).isNotNull();
		assertThat(createdUser.getCreatedTime()).isNotNull();
	}
	
	@Test
	@Transactional
	public void update() {
		// given
		User user = setupUser("Jakub", "Walczak", "jwalczak@gmail.com");
		UserDto userDto = new UserDto(user);
		userDto.setFirstName("Paulina");
		userDto.setLastName("Wach");
		userDto.setEmail("pwach@poczta.pl");
		// when
		UserDto result = userController.update(userDto.getId(), userDto);	
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		
		User updatedUser = load(User.class, result.getId());
		assertThat(updatedUser.getFirstName()).isEqualTo(userDto.getFirstName());
		assertThat(updatedUser.getLastName()).isEqualTo(userDto.getLastName());
		assertThat(updatedUser.getEmail()).isEqualTo(userDto.getEmail());
		assertThat(updatedUser.getModifiedById()).isNotNull();
		assertThat(updatedUser.getModifiedTime()).isNotNull();
	}
	
	@Test
	@Transactional
	public void exportToXlsx() {
		// when
		ResponseEntity<ByteArrayResource> result = userController.exportToXlsx();
		// then
		assertThat(result).isNotNull();
		assertThat(result.getBody().contentLength()).isGreaterThan(0);
		
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void assertUser(UserDto userDto, User user) {
		assertThat(userDto.getId()).isEqualTo(user.getId());
		assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
		assertThat(userDto.getLastName()).isEqualTo(user.getLastName());
		assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
		assertThat(userDto.getCreatedTime()).isEqualTo(user.getCreatedTime());
		assertThat(userDto.getCreatedByName()).isEqualTo(user.getCreatedBy().calcFirstLastName());
		assertThat(userDto.getModifiedTime()).isEqualTo(user.getModifiedTime());
		assertThat(userDto.getModifiedByName()).isEqualTo(user.getModifiedBy().calcFirstLastName());
	}
	
	private User setupUser(String firstName, String lastName, String email) {
		return new UserBuilder()
			.firstName(firstName)
			.lastName(lastName)
			.email(email)
			.saveAndReload(entityManager);
	}
	
	private UserDto setupUserDto(String firstName, String lastName, String email) {
		return new UserDto(
			new UserBuilder()
				.firstName(firstName)
				.lastName(lastName)
				.email(email)
				.build()
			);
	}
}
