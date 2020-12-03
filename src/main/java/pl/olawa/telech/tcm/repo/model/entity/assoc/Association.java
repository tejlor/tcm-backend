package pl.olawa.telech.tcm.repo.model.entity.assoc;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.administration.model.entity.User;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

/*
 * Association (connection) between two elements of repository. Base class.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("A")
public class Association extends AbstractEntity {

	@Column(insertable = false, updatable = false)
	Integer parentElementId;					// parent element
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentElementId", nullable = true)
	Element parentElement;
	
	@Column(insertable = false, updatable = false)
	Integer childElementId;						// child element
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "childElementId", nullable = true)
	Element childElement;
	
	@Column
	LocalDateTime createdTime;					// time of creation
	
	@Column(insertable = false, updatable = false)
	Integer createdById;						// user created element
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdById", nullable = true)
	private User createdBy;
	
	
	public Association(int id) {
		super(id);
	}
}
