package pl.olawa.telech.tcm.utils;

import java.util.Map;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import pl.olawa.telech.tcm.administration.logic.helper.interfaces.SimpleTokenEndpoint;

@TestConfiguration
public class TcmTestConfiguration {
 
    @Bean
    @Primary
    public SimpleTokenEndpoint createSimpleTokenEndpoint(){
        return new SimpleTokenEndpoint() {
			
			@Override
			public ResponseEntity<OAuth2AccessToken> postAccessToken(String clientId, Map<String, String> parameters) {
				throw new UnsupportedOperationException();
			}
		};
    }
}
