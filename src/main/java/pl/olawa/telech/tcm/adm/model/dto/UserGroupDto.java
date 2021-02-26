package pl.olawa.telech.tcm.adm.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.model.entity.UserGroup;
import pl.olawa.telech.tcm.commons.model.dto.AbstractModifiableDto;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UserGroupDto extends AbstractModifiableDto {

	String name;
	// out
	int userCount;
	
	
	public UserGroupDto(UserGroup model){
		super(model);
		userCount = model.getUsers().size();
	}

	@Override
	public UserGroup toModel() {
		var userGroup = new UserGroup();
		fillModel(userGroup);	
		return userGroup;
	}
	
	public static List<UserGroupDto> toDtoList(List<UserGroup> list){
		return toDtoList(UserGroup.class, UserGroupDto.class, list);
	}
	
	public static Set<UserGroupDto> toDtoSet(Set<UserGroup> set){
		return toDtoSet(UserGroup.class, UserGroupDto.class, set);
	}
}
