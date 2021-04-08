package pl.olawa.telech.tcm.repo.builder;

import static lombok.AccessLevel.PRIVATE;

import java.util.Set;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.adm.model.entity.UserGroup;
import pl.olawa.telech.tcm.commons.builder.AbstractBuilder;
import pl.olawa.telech.tcm.repo.model.entity.AccessRight;

@Setter
@FieldDefaults(level = PRIVATE)
@Accessors(chain = true, fluent = true)
public class AccessRightBuilder extends AbstractBuilder<AccessRight> {

	int elementId;					
	int orderNo;						
	Set<User> users;			
	Set<UserGroup> userGroups;		
	boolean canCreate;					
	boolean canRead;					
	boolean canUpdate;					
	boolean canDelete;					
	
	@Override
	public AccessRight build() {
		var obj = new AccessRight();
		super.fill(obj);
		obj.setElementId(elementId);
		obj.setOrderNo(orderNo);
		obj.setUsers(users);
		obj.setUserGroups(userGroups);
		obj.setForAll(users.isEmpty() && userGroups.isEmpty());
		obj.setCanCreate(canCreate);
		obj.setCanRead(canRead);
		obj.setCanUpdate(canUpdate);
		obj.setCanDelete(canDelete);
		return obj;	
	}
}
