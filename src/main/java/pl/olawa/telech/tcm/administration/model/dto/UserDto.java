package pl.olawa.telech.tcm.administration.model.dto;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.administration.model.entity.User;
import pl.olawa.telech.tcm.commons.model.dto.AbstractDto;


@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UserDto extends AbstractDto {

	String email;
	String firstName;
	String lastName;
	LocalDateTime createdTime;
	String createdByName;
	LocalDateTime modifiedTime;
	String modifiedByName;
	
	
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
