package pl.olawa.telech.tcm.adm.builder;

import static lombok.AccessLevel.PRIVATE;

import java.util.HashSet;
import java.util.Set;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.adm.model.entity.UserGroup;
import pl.olawa.telech.tcm.commons.builder.AbstractModifiableBuilder;
import pl.olawa.telech.tcm.commons.utils.TUtils;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class UserBuilder extends AbstractModifiableBuilder<User> {
	
	public static final String DEFAULT_PASSWORD = "password";
	
	String firstName = "Adam";				
	String lastName = "Nowakowski";				
	String email = "adam.nowakowski@gmail.com";					
	String password = TUtils.sha1(DEFAULT_PASSWORD);	
	Set<UserGroup> groups = new HashSet<>();
	
	@Override
	public User build() {
		var obj = new User();
		super.fill(obj);
		obj.setFirstName(firstName);
		obj.setLastName(lastName);
		obj.setEmail(email);
		obj.setPassword(password);
		obj.setGroups(groups);
		return obj;	
	}
}
