package pl.olawa.telech.tcm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PassChangeDto {
	
	private String oldPassword;
	private String newPassword;
	
}
