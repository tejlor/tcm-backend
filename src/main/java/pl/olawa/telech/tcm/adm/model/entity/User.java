package pl.olawa.telech.tcm.adm.model.entity;

import static lombok.AccessLevel.PRIVATE;

import java.util.*;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.entity.AbstractModifiableEntity;

/*
 * System user
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Table(name = "user", schema = "adm")
public class User extends AbstractModifiableEntity implements UserDetails {
		
	public static final String PROP_FIRST_NAME = "firstName";
	public static final String PROP_LAST_NAME = "lastName";
	public static final String PROP_EMAIL = "email";
	
	@Column(length = 32, nullable = false)
	String firstName;				// first name
	
	@Column(length = 32, nullable = false)
	String lastName;				// last name
	
	@Column(length = 64, nullable = false, unique = true)
	String email;					// email address
	
	@Column(length = 40, nullable = false)
	String password;				// password (hash SHA1)
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "User2UserGroup", schema = "adm",
		joinColumns = { @JoinColumn(name = "user_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "user_group_id") })
	Set<UserGroup> groups;
			

	public User(Integer id) {
		super(id);
		groups = new HashSet<>();
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

	// UserDetails
	
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
