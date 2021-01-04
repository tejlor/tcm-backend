package pl.olawa.telech.tcm.adm.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.builder.UserBuilder;
import pl.olawa.telech.tcm.adm.model.dto.UserDto;
import pl.olawa.telech.tcm.adm.model.entity.User;
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
}
