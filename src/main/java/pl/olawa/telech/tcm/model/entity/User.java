package pl.olawa.telech.tcm.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

/*
 * Użytkownik systemu.
 */
@Entity
@Getter @Setter
public class User extends AbstractEntity implements UserDetails {
		
	
	@Column
	private String firstName;				// imię
	
	@Column
	private String lastName;				// nazwisko
	
	@Column
	private String email;					// adres email
	
	@Column
	private String password;				// hasło
	
	@Column
	private Long createdById;				// osoba, która tworzyła rekord
	
	@Column
	private LocalDateTime createdTime;		// czas utworzenia rekordu
	
	@Column
	private Long modifiedById;				// osoba, która ostation modyfikowała rekord
	
	@Column
	private LocalDateTime modifiedTime;		// czas ostatniej modyfikacji
		

	public User(){
		super();
	}

	public User(Long id) {
		super(id);
	}

	public String calcLastFirstName() {
		return lastName + " " + firstName;
	}

	public String calcFirstLastName() {
		return firstName + " " + lastName;
	}
	
	public String calcShortName(){
		return firstName.substring(0, 1) + "." + lastName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list = new ArrayList<>();
		/*
		if(roleTimeEntry)
			list.add(new SimpleGrantedAuthority("ROLE_TIME_ENTRY"));
		*/
		
		return list;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
