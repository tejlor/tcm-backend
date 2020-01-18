package pl.olawa.telech.tcm.dto.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.model.entity.User;


@Getter @Setter
@NoArgsConstructor
public class UserDto extends AbstractDto {

	private String email;
	private String firstName;
	private String lastName;
	private LocalDateTime createdTime;
	private String createdByName;
	private LocalDateTime modifiedTime;
	private String modifiedByName;

	
	public UserDto(User model){
		super(model);
	}

	@Override
	public User toModel() {
		User user = new User();
		fillModel(user);
		
		return user;
	}
	
	public static List<UserDto> toDtoList(List<User> list){
		return toDtoList(User.class, UserDto.class, list);
	}

}