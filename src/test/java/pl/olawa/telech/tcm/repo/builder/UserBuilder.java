package pl.olawa.telech.tcm.repo.builder;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.administration.model.entity.User;

@Setter
@Accessors(chain = true, fluent = true)
@FieldDefaults(level = PRIVATE)
public class UserBuilder {
	
	String firstName = "Adam";				
	String lastName = "Nowakowski";				
	String email = "adam.nowakowski@gmail.com";					
	String password = "sha1";				
	Integer createdById = 1;				
	LocalDateTime createdTime = LocalDateTime.now();		
	Integer modifiedById = 1;				
	LocalDateTime modifiedTime = LocalDateTime.now();		
	
	public User build() {
		var user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(password);
		user.setCreatedById(createdById);
		user.setCreatedTime(createdTime);
		user.setModifiedById(modifiedById);
		user.setModifiedTime(modifiedTime);
		return user;	
	}
	
	public User save(EntityManager em) {
		var result = build();
		em.persist(result);
		return result;
	}
}
