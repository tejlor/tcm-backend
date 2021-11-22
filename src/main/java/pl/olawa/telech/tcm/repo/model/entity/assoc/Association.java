package pl.olawa.telech.tcm.repo.model.entity.assoc;

import static lombok.AccessLevel.PRIVATE;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import pl.olawa.telech.tcm.commons.model.entity.AbstractCreatableEntity;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

/*
 * Association (connection) between two elements of repository. Base class.
 */
@Entity
@Getter
@NoArgsConstructor
@FieldNameConstants
@FieldDefaults(level = PRIVATE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue("A")
@Table(name = "association", schema = "repo")
public class Association extends AbstractCreatableEntity {

	public static final String PROP_PARENT_ELEMENT_ID = "parentElementId"; 
	public static final String PROP_CHILD_ELEMENT_ID = "childElementId"; 
	
	@Column(insertable = false, updatable = false)
	Integer parentElementId;					// parent element
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parentElementId", nullable = true)
	Element parentElement;
	
	@Column(insertable = false, updatable = false)
	Integer childElementId;						// child element
	
	@ManyToOne(fetch = FetchType.EAGER)
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
