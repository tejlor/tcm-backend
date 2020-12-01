package pl.olawa.telech.tcm.administration.logic.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import pl.olawa.telech.tcm.administration.model.entity.User;

public interface AccountLogic {

	User getCurrentUser();
	User loadCurrentUser();
	ResponseEntity<OAuth2AccessToken> loginAs(int userId);
	void changePassword(String oldPassword, String newPassword);
}
