package pl.olawa.telech.tcm.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import pl.olawa.telech.tcm.dto.PassChangeDto;
import pl.olawa.telech.tcm.dto.entity.UserDto;
import pl.olawa.telech.tcm.logic.AccountLogic;


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