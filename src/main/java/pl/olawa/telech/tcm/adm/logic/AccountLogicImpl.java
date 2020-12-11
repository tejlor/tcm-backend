package pl.olawa.telech.tcm.adm.logic;

import static lombok.AccessLevel.PRIVATE;

import java.util.HashMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.adm.dao.UserDAO;
import pl.olawa.telech.tcm.adm.logic.helper.interfaces.SimpleTokenEndpoint;
import pl.olawa.telech.tcm.adm.logic.interfaces.AccountLogic;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.commons.model.exception.TcmException;
import pl.olawa.telech.tcm.commons.utils.TUtils;

@Slf4j
@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class AccountLogicImpl extends AbstractLogicImpl<User> implements AccountLogic, UserDetailsService {

	@Value("${tcm.auth.clientName}")
	String clientName;
	@Value("${tcm.auth.clientPass}")
	String clientPass;
	
	UserDAO dao;
	
	@Autowired
	SimpleTokenEndpoint tokenEndpoint;

	
	public AccountLogicImpl(UserDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	/*
	 * Gets logged user from session.
	 * Object isn't connected to Hibernate session and doesn't have sub-objects.
	 */
	@Override
	public User getCurrentUser() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Object principal = authentication.getPrincipal();

			if (principal instanceof User) {
				log.debug("get " + ((User)principal).toFullString(0));
				return (User) principal;
			}
			else {
				//return null;
				return new User(1); 
			}
		}
		catch (Exception e) {
			log.warn("Principal is null: " + e.getMessage());
			//return null;
			return new User(1); 
		}
	}
	
	@Override
	public User loadCurrentUser() {
		return dao.findById(getCurrentUser().getId()).get();
	}
		
	@Override
	public ResponseEntity<OAuth2AccessToken> loginAs(int userId){
		User user = loadById(userId);
		return generateTokenForUser(user);
	}
	
	@Override
	public void changePassword(String oldPassword, String newPassword){
		validateNewPassword(newPassword);
		
		User user = loadCurrentUser();	
		String encodedOldPassword = TUtils.sha1(oldPassword);
		String encodedNewPassword = TUtils.sha1(newPassword);		
		if(user.getPassword().compareToIgnoreCase(encodedOldPassword) == 0){
			user.setPassword(encodedNewPassword);
			save(user);
		}
		else {
			throw new TcmException("The old password is incorrect.");
		}
	}
	
	// UserDetailsService
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = dao.findByEmail(username);
		if(user == null)
			throw new UsernameNotFoundException(username);
		
		log.debug("load " + user.toFullString(0));
		return user;
	}

	// #################################### PRIVATE ###################################################################################

	private ResponseEntity<OAuth2AccessToken> generateTokenForUser(User user){
		ResponseEntity<OAuth2AccessToken> res = null;
		
		try {			
	        HashMap<String, String> parameters = new HashMap<String, String>();
	        parameters.put("grant_type", "password");
	        parameters.put("password", "");
	        parameters.put("username", user.getEmail());        
	        res = tokenEndpoint.postAccessToken(clientName, parameters);
		}
        catch(Exception e){
        	log.error(ExceptionUtils.getStackTrace(e));
        	throw new TcmException("Error during token generation.");
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
		log.debug("Chars in new pass: " + numbers + " " + lowers + " " + uppers);
		if(numbers < 2 || lowers < 2 || uppers < 2)
			throw new TcmException("Password should have at least 2 lower letters, 2 upper letters and 2 digits.");
	}
}
