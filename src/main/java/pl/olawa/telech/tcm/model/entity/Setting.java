package pl.olawa.telech.tcm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * System setting.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "setting", schema = "public")
public class Setting extends AbstractEntity {
	
	public static final String TRASH_REF = "trash_ref";
	
		
	@Column
	private String name;				// setting name
	
	@Column
	private String value;				// setting value
	

	public Setting(Integer id) {
		super(id);
	}

}
