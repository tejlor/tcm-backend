package pl.olawa.telech.tcm.repo.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.administration.controller.UserController;
import pl.olawa.telech.tcm.administration.model.dto.UserDto;
import pl.olawa.telech.tcm.administration.model.entity.User;
import pl.olawa.telech.tcm.repo.builder.UserBuilder;
import pl.olawa.telech.tcm.utils.BaseTest;


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
		userList.add(setupUser("Paułina", "Wach", "pwach@gmail.com"));
		userList.add(setupUser("Bolesław", "Kowalski", "bkowalski@gmail.com"));
		flush();	
		// when
		List<UserDto> result = userController.getAll();	
		// then
		assertThat(result).isNotNull();
		assertThat(result.size()).isEqualTo(userList.size());
		for(int i = 0; i < userList.size(); i++) {
			assertUser(result.get(0), userList.get(0));
		}
	}
	
	private void assertUser(UserDto userDto, User user) {
		assertThat(userDto.getId()).isEqualTo(user.getId());
		assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
		assertThat(userDto.getLastName()).isEqualTo(user.getLastName());
		assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
		assertThat(userDto.getCreatedTime()).isEqualTo(user.getCreatedTime());
		assertThat(userDto.getCreatedByName()).isNull();
		assertThat(userDto.getModifiedTime()).isEqualTo(user.getModifiedTime());
		assertThat(userDto.getModifiedByName()).isNull();
	}
	
	private User setupUser(String firstName, String lastName, String email) {
		return new UserBuilder()
			.firstName(firstName)
			.lastName(lastName)
			.email(email)
			.save(entityManager);
	}
}
