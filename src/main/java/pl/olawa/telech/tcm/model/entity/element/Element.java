package pl.olawa.telech.tcm.model.entity.element;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.model.entity.User;

/*
 * Element repozytoriom. Klasa bazowa dla wszystkich typów.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Element extends AbstractEntity {

	@Column
	private UUID ref;						// unikalna referencja obiektu
	
	@Column
	private String name;					// nazwa elementu
	
	@Column
	private LocalDateTime createdTime;		// data utworzenia
	
	@Column(insertable = false, updatable = false)
	private Long createdById;				// użytkownik tworzący element
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdById", nullable = true)
	private User createdBy;
	
	@Column
	private LocalDateTime modifiedTime;		// data ostatniej modyfikacji elementu
	
	@Column(insertable = false, updatable = false)
	private Long modifiedById;				// użytkownik ostatnio modyfikujący element
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifiedById", nullable = true)
	private User modifiedBy;
	
	
	
	public Element(Integer id) {
		super(id);
	}
	
	public String getTypeName() {
		return "Element";
	}
}
