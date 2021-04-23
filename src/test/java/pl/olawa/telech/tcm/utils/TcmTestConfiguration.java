package pl.olawa.telech.tcm.utils;

import java.util.Map;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import pl.olawa.telech.tcm.adm.builder.UserBuilder;
import pl.olawa.telech.tcm.adm.logic.helper.interfaces.SimpleTokenEndpoint;
import pl.olawa.telech.tcm.adm.logic.interfaces.AccountLogic;
import pl.olawa.telech.tcm.adm.model.entity.User;

@TestConfiguration
public class TcmTestConfiguration {
 	
    @Bean
    @Primary
    public SimpleTokenEndpoint createSimpleTokenEndpoint(){
        return new SimpleTokenEndpoint() {
			
			@Override
			public ResponseEntity<OAuth2AccessToken> postAccessToken(String clientId, Map<String, String> parameters) {		
				HttpHeaders headers = new HttpHeaders();
				headers.set("Cache-Control", "no-store");
				headers.set("Pragma", "no-cache");
				headers.set("Content-Type", "application/json;charset=UTF-8");
				return new ResponseEntity<OAuth2AccessToken>(new DefaultOAuth2AccessToken("token"), headers, HttpStatus.OK);
			}
		};
    }
    
    @Bean
    @Primary
    public AccountLogic createAccountLogic(){
        return new AccountLogic() {
			
        	private User user = new UserBuilder().id(1).build();   	
        	
			@Override
			public User getCurrentUser() {
				return user;
			}

			@Override
			public User loadCurrentUser() {
				return user;
			}

			@Override
			public ResponseEntity<OAuth2AccessToken> loginAs(int userId) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void changePassword(String oldPassword, String newPassword) {
				throw new UnsupportedOperationException();
			}
		};
    }
}
