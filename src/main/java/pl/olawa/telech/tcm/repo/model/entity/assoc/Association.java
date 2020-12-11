package pl.olawa.telech.tcm.repo.model.entity.assoc;

import static lombok.AccessLevel.PRIVATE;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.commons.model.entity.AbstractCreatableEntity;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

/*
 * Association (connection) between two elements of repository. Base class.
 */
@Entity
@Getter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("A")
@Table(name = "association", schema = "repo")
public class Association extends AbstractCreatableEntity {

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
	
	
	public Association(int id) {
		super(id);
	}
	
	public void setParentElement(Element element) {
		parentElement = element;
		parentElementId = element != null ? element.getId() : null;
	}
	
	public void setChildElement(Element element) {
		childElement = element;
		childElementId = element != null ? element.getId() : null;
	}
}
