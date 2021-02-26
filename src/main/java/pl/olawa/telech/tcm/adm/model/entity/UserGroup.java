package pl.olawa.telech.tcm.adm.model.entity;

import static lombok.AccessLevel.PRIVATE;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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
@Table(name = "user_group", schema = "adm")
public class UserGroup extends AbstractModifiableEntity {
		
	public static final String PROP_NAME = "name";
	
	@Column(length = 32, nullable = false)
	String name;				// name
	
	@ManyToMany(mappedBy = "groups")
	Set<User> users = new HashSet<>();
			

	public UserGroup(Integer id) {
		super(id);
		users = new HashSet<>();
	}
}
