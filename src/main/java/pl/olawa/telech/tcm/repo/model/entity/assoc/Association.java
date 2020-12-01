package pl.olawa.telech.tcm.repo.model.entity.assoc;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.administration.model.entity.User;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

/*
 * Association (connection) between two elements of repository. Base class.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("A")
public class Association extends AbstractEntity {

	@Column(insertable = false, updatable = false)
	private Integer parentElementId;					// element rodzica
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentElementId", nullable = true)
	private Element parentElement;
	
	@Column(insertable = false, updatable = false)
	private Integer childElementId;						// element dzecka
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "childElementId", nullable = true)
	private Element childElement;
	
	@Column
	private LocalDateTime createdTime;					// data utworzenia
	
	@Column(insertable = false, updatable = false)
	private Integer createdById;						// użytkownik tworzący element
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdById", nullable = true)
	private User createdBy;
	
	
	public Association(int id) {
		super(id);
	}

}
