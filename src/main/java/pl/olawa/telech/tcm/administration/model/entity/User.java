package pl.olawa.telech.tcm.administration.model.entity;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;

/*
 * System user
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Table(name = "user", schema = "public")
public class User extends AbstractEntity implements UserDetails {
		
	@Column(length = 32, nullable = false)
	String firstName;				// first name
	
	@Column(length = 32, nullable = false)
	String lastName;				// last name
	
	@Column(length = 64, nullable = false)
	String email;					// email address
	
	@Column(length = 40, nullable = false)
	String password;				// password
	
	@Column(nullable = false)
	Integer createdById;			// user who created the record
	
	@Column(nullable = false)
	LocalDateTime createdTime;		// record creation date and time
	
	@Column
	Integer modifiedById;			// user who last modified the record
	
	@Column
	LocalDateTime modifiedTime;		// record modification date and time
		

	public User(Integer id) {
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
