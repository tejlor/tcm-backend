package pl.olawa.telech.tcm.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/*
 * Połączenie pomiędzy dwoma elementami repozytorium. Klasa bazowa.
 */
@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class Association extends AbstractEntity {

	
	@Column(insertable = false, updatable = false)
	private Long parentElementId;					// element rodzica
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentElementId", nullable = true)
	private Element parentElement;
	
	@Column(insertable = false, updatable = false)
	private Long childElementId;					// element dzecka
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "childElementId", nullable = true)
	private Element childElement;
	
	
	public Association() {
		
	}
	
	public Association(Long id) {
		super(id);
	}
}
