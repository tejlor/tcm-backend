package pl.olawa.telech.tcm.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import pl.olawa.telech.tcm.dto.PassChangeDto;
import pl.olawa.telech.tcm.dto.entity.EmployeeDto;
import pl.olawa.telech.tcm.logic.AccountLogic;


@RestController
@RequestMapping("/Accounts")
public class AccountController extends AbstractController {

	@Autowired
	private AccountLogic accountLogic;

	
	/*
	 * Zwraca aktualnie zalogowane użytkownika celem sprawdzania uprawnień i wczytywania danych.
	 */
	@RequestMapping(value = "/Current", method = RequestMethod.GET)
	public EmployeeDto getCurrentUser() {
		
		return new EmployeeDto(accountLogic.loadCurrentUser());
	}
	
	/*
	 * Loguje użytkownika jako wybrany pracownik.
	 */
	@RolesAllowed("ADMIN")
	@RequestMapping(value = "/LoginAs", method = RequestMethod.POST)
	public ResponseEntity<OAuth2AccessToken> loginAs(
			@RequestParam(required = true) long userId) {
		
		return accountLogic.loginAs(userId);
	}
	
	/*
	 * Zmienia hasło dla bieżącego użytkownika.
	 */
	@RequestMapping(value = "/ChangePassword", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void changePass(
			@RequestBody(required = true) PassChangeDto passChange) {
		
		accountLogic.changePassword(passChange.getOldPassword(), passChange.getNewPassword());
	}

}