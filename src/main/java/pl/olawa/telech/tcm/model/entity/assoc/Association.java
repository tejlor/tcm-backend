package pl.olawa.telech.tcm.model.entity.assoc;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.model.entity.element.Element;

/*
 * Połączenie pomiędzy dwoma elementami repozytorium. Klasa bazowa.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
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
	
	
	public Association(Integer id) {
		super(id);
	}
}
