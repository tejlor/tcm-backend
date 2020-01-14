package pl.olawa.telech.tcm.dto.entity;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.model.entity.User;


@Getter @Setter
@NoArgsConstructor
public class EmployeeDto extends AbstractDto {

	private String email;
	private String firstName;
	private String lastName;
	private String contractType;
	private String contractTypeKey;
	private String businessUnit;
	private Boolean roleTimeEntry;
	private Boolean roleManager;
	private Boolean roleControlling;
	private Boolean roleAdmin;
	private Boolean roleCreativeWork;
	private String position;
	private Double employmentTime;
	private Long bossId;
	private String bossName;
	private Boolean active;
	private Integer index;

	
	public EmployeeDto(User model){
		super(model);
	}

	@Override
	public User toModel() {
		throw new UnsupportedOperationException();
	}
	
	public static List<EmployeeDto> toDtoList(List<User> list){
		return toDtoList(User.class, EmployeeDto.class, list);
	}

}