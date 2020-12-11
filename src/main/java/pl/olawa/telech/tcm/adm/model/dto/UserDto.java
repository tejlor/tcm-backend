package pl.olawa.telech.tcm.adm.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.commons.model.dto.AbstractModifiableDto;


@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UserDto extends AbstractModifiableDto {

	String firstName;
	String lastName;
	String email;
	
	
	public UserDto(User model){
		super(model);
	}

	@Override
	public User toModel() {
		User user = new User();
		fillModel(user);
		
		// TODO
		// createdByname, modifiedByName
		
		return user;
	}
	
	public static List<UserDto> toDtoList(List<User> list){
		return toDtoList(User.class, UserDto.class, list);
	}
}
