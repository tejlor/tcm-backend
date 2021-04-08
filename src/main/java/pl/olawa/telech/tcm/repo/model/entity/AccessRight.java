package pl.olawa.telech.tcm.repo.model.entity;

import static lombok.AccessLevel.PRIVATE;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.adm.model.entity.UserGroup;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;

@Entity
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Table(name = "access_right", schema = "repo")
public class AccessRight extends AbstractEntity {
	
	public static final String PROP_ELEMENT_ID = "elementId";
	public static final String PROP_ORDER = "order";
	
	@Column(nullable = false)
	int elementId;						// element's id
	
	@Column(nullable = false)
	int orderNo;						// order of current access right at list of all element's access rights
	
	@Column(nullable = false)
	boolean forAll;						// order of current access right at list of all element's access rights
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "User2AccessRight", schema = "repo",
		joinColumns = { @JoinColumn(name = "access_right_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "user_id") })
	Set<User> users;					// selected users
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "UserGroup2AccessRight", schema = "repo",
		joinColumns = { @JoinColumn(name = "access_right_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "user_group_id") })
	Set<UserGroup> userGroups;			// selected user groups
	
	@Column(nullable = false)
	boolean canCreate;					// can create new elements
	
	@Column(nullable = false)
	boolean canRead;					// can read element properties and content
	
	@Column(nullable = false)
	boolean canUpdate;					// can modify element properties and content
	
	@Column(nullable = false)
	boolean canDelete;					// can delete element
	
	
	public AccessRight(int id) {
		super(id);
		users = new HashSet<>();
		userGroups = new HashSet<>();
	}
	
	public boolean isValid(){
		if(users.isEmpty() && userGroups.isEmpty() && !forAll) {
			return false;
		}

		return true;
	}
	
}
