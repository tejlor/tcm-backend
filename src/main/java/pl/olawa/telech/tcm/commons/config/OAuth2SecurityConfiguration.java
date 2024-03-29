package pl.olawa.telech.tcm.commons.config;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.logic.AccountLogicImpl;
import pl.olawa.telech.tcm.commons.utils.Sha1PasswordEncoder;
import pl.olawa.telech.tcm.commons.utils.TUtils;

/*
 * Configuration of oAuth 2.0.
 */
@Configuration
@EnableWebSecurity
@FieldDefaults(level = PRIVATE)
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${tcm.environment}")
	String environment;
	
	@Autowired
	AccountLogicImpl accountLogic;
	
	@Bean
	public AuthenticationProvider authProvider() {
	    var authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService((UserDetailsService) accountLogic);
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    auth.authenticationProvider(authProvider());
	}
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
		http
			.anonymous().disable()
			.authorizeRequests()
			.antMatchers("/oauth/token").permitAll();
    }	

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {	
		var config = new CorsConfiguration();
		config.setAllowCredentials(true);
		if(!TUtils.isProd(environment)){
			config.addAllowedOrigin("*");
		}
		config.addAllowedHeader("*");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("OPTIONS");
		config.setMaxAge(3600L);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Bean
    public static PasswordEncoder passwordEncoder() {
    	return new Sha1PasswordEncoder();
    }
}
