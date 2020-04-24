package pl.olawa.telech.tcm.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pl.olawa.telech.tcm.logic.AccountLogic;
import pl.olawa.telech.tcm.model.dto.PassChangeDto;
import pl.olawa.telech.tcm.model.dto.entity.UserDto;


@RestController
@RequestMapping("/accounts")
public class AccountController extends AbstractController {

	@Autowired
	private AccountLogic accountLogic;

	
	/*
	 * Zwraca aktualnie zalogowanego użytkownika.
	 */
	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public UserDto getCurrentUser() {
		
		return new UserDto(accountLogic.loadCurrentUser());
	}
	
	/*
	 * Loguje użytkownika na wybrane konto.
	 */
	@RolesAllowed("ADMIN")
	@RequestMapping(value = "/loginAs", method = RequestMethod.POST)
	public ResponseEntity<OAuth2AccessToken> loginAs(
			@RequestParam(required = true) int userId) {
		
		return accountLogic.loginAs(userId);
	}
	
	/*
	 * Zmienia hasło dla bieżącego użytkownika.
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void changePass(
			@RequestBody(required = true) PassChangeDto passChange) {
		
		accountLogic.changePassword(passChange.getOldPassword(), passChange.getNewPassword());
	}

}