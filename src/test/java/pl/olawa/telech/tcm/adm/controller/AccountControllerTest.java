package pl.olawa.telech.tcm.adm.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.builder.UserBuilder;
import pl.olawa.telech.tcm.adm.logic.interfaces.AccountLogic;
import pl.olawa.telech.tcm.adm.model.dto.PassChangeDto;
import pl.olawa.telech.tcm.adm.model.dto.UserDto;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.commons.model.exception.TcmException;
import pl.olawa.telech.tcm.commons.utils.TUtils;
import pl.olawa.telech.tcm.utils.BaseTest;

@RunWith(SpringRunner.class)
@FieldDefaults(level = PRIVATE)
public class AccountControllerTest extends BaseTest {

	@Autowired
	AccountController accountController;
	
	@Test
	@Transactional
	public void getCurrentUser() {	
		// given
		flush();
		// when
		UserDto result = accountController.getCurrentUser();	
		flushAndClear();
		// then
		assertThat(result).isNotNull();
		assertCurrentUser(result, defaultUser);
	}
	
	@Test
	@Transactional
	public void loginAs() {
		// given
		User user = setupUser("Jakub", "Walczak", "jwalczak@gmail.com");
		flush();	
		// when
		ResponseEntity<OAuth2AccessToken> result = accountController.loginAs(user.getId());	
		flushAndClear();
		// then
		assertThat(result).isNotNull();		
		OAuth2AccessToken body = result.getBody();
		assertThat(body.getValue()).isNotEmpty();
	}
	
	@Test
	@Transactional
	public void changePassword() {
		// given
		String oldPassword = UserBuilder.DEFAULT_PASSWORD;
		String newPassword = "NewPass123";
		// when
		var in = new PassChangeDto();
		in.setOldPassword(oldPassword);
		in.setNewPassword(newPassword);
		accountController.changePassword(in);	
		flushAndClear();
		// then	
		assertThat(defaultUser.getPassword()).isNotEqualTo(TUtils.sha1(oldPassword));
		assertThat(defaultUser.getPassword()).isEqualTo(TUtils.sha1(newPassword));
	}
	
	@Test
	@Transactional
	public void changePassword_incorrectOldPass() {
		// given
		String oldPassword = "incorrectPassword";
		String newPassword = "NewPass123";
		// when
		var in = new PassChangeDto();
		in.setOldPassword(oldPassword);
		in.setNewPassword(newPassword);
		expectException(() -> 
			accountController.changePassword(in), 
				TcmException.class, "The old password is incorrect."
		);	
	}
	
	@Test
	@Transactional
	public void changePassword_toSimpleNewPass() {
		// given
		String oldPassword = UserBuilder.DEFAULT_PASSWORD;
		String newPassword = "newPass";
		// when
		var in = new PassChangeDto();
		in.setOldPassword(oldPassword);
		in.setNewPassword(newPassword);
		expectException(() -> 
			accountController.changePassword(in), 
				TcmException.class, "Password should have at least 2 lower letters, 2 upper letters and 2 digits."
		);	
	}
	
	// ################################### PRIVATE #########################################################################

	private User setupUser(String firstName, String lastName, String email) {
		return new UserBuilder()
			.firstName(firstName)
			.lastName(lastName)
			.email(email)
			.save(entityManager);
	}
	
	private void assertCurrentUser(UserDto userDto, User user) {
		assertThat(userDto.getId()).isEqualTo(user.getId());
		assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
		assertThat(userDto.getLastName()).isEqualTo(user.getLastName());
		assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
	}
	
	@SuppressWarnings("unused")
	private AccountLogic mockAccountLogic(User currentUser) {
		var accountLogic = mock(AccountLogic.class);
		when(accountLogic.getCurrentUser()).thenReturn(currentUser);
		setBeanField(accountController, "accountLogic", accountLogic);
		return accountLogic;
	}
}
