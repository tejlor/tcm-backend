package pl.olawa.telech.tcm.adm.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.logic.AccountLogicImpl;
import pl.olawa.telech.tcm.adm.model.dto.PassChangeDto;
import pl.olawa.telech.tcm.adm.model.dto.UserDto;
import pl.olawa.telech.tcm.commons.controller.AbstractController;


@RestController
@RequestMapping("/accounts")
@FieldDefaults(level = PRIVATE)
public class AccountController extends AbstractController {

	@Autowired
	AccountLogicImpl accountLogic;
	
	/*
	 * Returns current user.
	 */
	@RequestMapping(value = "/current", method = GET)
	public UserDto getCurrentUser() {
		
		return new UserDto(accountLogic.loadCurrentUser());
	}
	
	/*
	 * Logins user as selected diferent user.
	 */
	//@RolesAllowed("ADMIN")
	@RequestMapping(value = "/loginAs", method = POST)
	public ResponseEntity<OAuth2AccessToken> loginAs(
			@RequestParam int userId) {
		
		return accountLogic.loginAs(userId);
	}
	
	/*
	 * Changes password for current user.
	 */
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/changePassword", method = POST)
	public void changePassword(
			@RequestBody(required = true) PassChangeDto passChange) {
		
		accountLogic.changePassword(passChange.getOldPassword(), passChange.getNewPassword());
	}
}
