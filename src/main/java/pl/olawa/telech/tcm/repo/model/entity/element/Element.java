package pl.olawa.telech.tcm.repo.model.entity.element;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.administration.model.entity.User;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.repo.model.entity.assoc.ContainsAssoc;

/*
 * Element of the repository. Base class for all elements.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Inheritance(strategy = InheritanceType.JOINED)
public class Element extends AbstractEntity {

	@Column
	UUID ref;						// element unique reference
	
	@Column
	String name;					// element name
	
	@Column
	LocalDateTime createdTime;		// date of creation the element
	
	@Column(insertable = false, updatable = false)
	Integer createdById;			// user who created the element
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdById", nullable = true)
	User createdBy;
	
	@Column
	LocalDateTime modifiedTime;		// date of last modification
	
	@Column(insertable = false, updatable = false)
	Integer modifiedById;			// user who last modified the element
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifiedById", nullable = true)
	User modifiedBy;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "childElementId")
	List<ContainsAssoc> parents;		// parents of the element
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentElementId")
	List<ContainsAssoc> children;		// children of the element
	
	
	@Transient
	UUID parentRef;
	
	@Transient
	List<Element> childrenElements;
 	
	
	public Element(int id) {
		super(id);
	}
	
	public String getTypeName() {
		return "Element";
	}
	
	public void attachCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		if(createdBy != null)
			this.createdById = createdBy.getId();
		else 
			this.createdById = null;
	}
	
	public void attachModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
		if(modifiedBy != null)
			this.modifiedById = modifiedBy.getId();
		else 
			this.modifiedById = null;
	}
	
	public Element copy() {
		throw new UnsupportedOperationException();
	}
	
	protected void fillCopy(Element copy) {
		copy.setName(name);
	}
}
