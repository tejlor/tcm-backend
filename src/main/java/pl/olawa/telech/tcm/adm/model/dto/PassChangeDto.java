package pl.olawa.telech.tcm.adm.model.dto;

import static lombok.AccessLevel.PRIVATE;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class PassChangeDto {
	
	String oldPassword;
	String newPassword;
}
