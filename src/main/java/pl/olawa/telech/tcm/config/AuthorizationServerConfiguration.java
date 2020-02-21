package pl.olawa.telech.tcm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import pl.olawa.telech.tcm.logic.AccountLogic;

/*
 * Konfiguracja uwierzytelniania oAuth 2.0.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Value("${tcm.auth.clientName}")
	private String clientName;
	
	@Value("${tcm.auth.clientPass}")
	private String clientPass;
	
	@Value("${tcm.auth.accessTokenExpTime}")
	private int accessTokenExpTime;
	
	@Value("${tcm.auth.refreshTokenExpTime}")
	private int refreshTokenExpTime;
	
	@Autowired
	private TokenStore tokenStore;

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AccountLogic accountLogic;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			.withClient(clientName)
			.authorizedGrantTypes("password", "refresh_token")
			.scopes("read")
			.secret(clientPass)
			.accessTokenValiditySeconds(accessTokenExpTime) 		
			.refreshTokenValiditySeconds(refreshTokenExpTime); 
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(tokenStore)
			.authenticationManager(authenticationManager).userDetailsService((UserDetailsService) accountLogic);
	}

}