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

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class UserGroupBuilder extends AbstractModifiableBuilder<UserGroup> {
	
	String name = "Użytkownicy";					
	Set<User> users = new HashSet<>();
	
	@Override
	public UserGroup build() {
		var obj = new UserGroup();
		super.fill(obj);
		obj.setName(name);
		obj.setUsers(users);
		return obj;	
	}
}
