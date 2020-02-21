package pl.olawa.telech.tcm.logic;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.dao.UserDAO;
import pl.olawa.telech.tcm.model.entity.User;
import pl.olawa.telech.tcm.model.exception.TcmException;
import pl.olawa.telech.tcm.utils.TUtils;

@Slf4j
@Service
@Transactional
public class AccountLogic extends AbstractLogic<User> implements UserDetailsService {

	@Value("${tcm.auth.clientName}")
	private String clientName;
	
	@Value("${tcm.auth.clientPass}")
	private String clientPass;
	
	private UserDAO dao;
	
	@Autowired
	private TokenEndpoint tokenEndpoint;

	
	public AccountLogic(UserDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = dao.findByEmail(username);
		if(user == null)
			throw new UsernameNotFoundException(username);
		
		return user;
	}
	
	/*
	 * Pobiera zalogowanego użytkownika z sesji.
	 * Obiekt nie jest podłączony do sesji Hibernate'owej, nie zawiera podobiektów.
	 */
	public User getCurrentUser() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Object principal = authentication.getPrincipal();

			if (principal instanceof User)
				return (User) principal;
			else
				//return null;
				return new User(1); 
		}
		catch (Exception e) {
			log.warn("Principal is null: " + e.getMessage());
			//return null;
			return new User(1); 
		}
	}
	
	public User loadCurrentUser() {
		return dao.findById(getCurrentUser().getId()).get();
	}
		
	public ResponseEntity<OAuth2AccessToken> loginAs(int userId){
		User user = loadById(userId);
		return generateTokenForUser(user);
	}
	
	public void changePassword(String oldPassword, String newPassword){
		User user = loadCurrentUser();
		
		validateNewPassword(newPassword);
		
		String encodedOldPassword = TUtils.sha1(oldPassword);
		String encodedNewPassword = TUtils.sha1(newPassword);
		
		if(user.getPassword().compareToIgnoreCase(encodedOldPassword) == 0){
			user.setPassword(encodedNewPassword);
			dao.save(user);
		}
		else {
			throw new TcmException("Stare hasło jest niepoprawne.");
		}
	}

	// #################################### PRIVATE ###################################################################################

	private ResponseEntity<OAuth2AccessToken> generateTokenForUser(User user){
		String tempPass = "abcdef"; // nie będzie nigdy zapisane do bazy, jest tylko w sesji
		ResponseEntity<OAuth2AccessToken> res = null;
		
		try {
			String oldPassword = user.getPassword();
			user.setPassword(TUtils.sha1(tempPass));
			
			Principal principal = new UsernamePasswordAuthenticationToken(clientName, clientPass, new ArrayList<GrantedAuthority>());
	        HashMap<String, String> parameters = new HashMap<String, String>();
	        parameters.put("grant_type", "password");
	        parameters.put("password", tempPass);
	        parameters.put("username", user.getEmail());
	        
	        try {
	        	res = tokenEndpoint.postAccessToken(principal, parameters);
	        }
	        catch(InvalidGrantException | DisabledException e) {
	        	log.error(ExceptionUtils.getStackTrace(e));
	        	throw new TcmException("Konto użytkownika zostało wyłączone.");
	        }
	
	        user.setPassword(oldPassword);
		}
        catch(HttpRequestMethodNotSupportedException e){
        	log.error(ExceptionUtils.getStackTrace(e));
        	throw new TcmException("Nie udało się wygenerować tokena.");
        }
        
        return res;
	}
	
	private void validateNewPassword(String password){
		int numbers = 0, lowers = 0, uppers = 0;
		
		for(char c : password.toCharArray()){
			if(c >= 'A' && c <= 'Z')
				uppers++;
			else if(c >= 'a' && c <= 'z')
				lowers++;
			else if(c >= '0' && c <= '9')
				numbers++;
		}
		log.debug("Znaki w nowym haśle: " + numbers + " " + lowers + " " + uppers);
		if(numbers < 2 || lowers < 2 || uppers < 2)
			throw new TcmException("Hasło powinno zawierać przynajmniej 2 małe litery, 2 duże litery i 2 cyfry.");
	}
	
}
