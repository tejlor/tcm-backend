package pl.olawa.telech.tcm.model.entity.element;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.olawa.telech.tcm.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.model.entity.User;
import pl.olawa.telech.tcm.model.entity.assoc.Association;

/*
 * Element of the repository. Base class for all elements.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Element extends AbstractEntity {

	@Column
	private UUID ref;						// element unique reference
	
	@Column
	private String name;					// element name
	
	@Column
	private LocalDateTime createdTime;		// date of creation the element
	
	@Column(insertable = false, updatable = false)
	private Integer createdById;			// user who created the element
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdById", nullable = true)
	private User createdBy;
	
	@Column
	private LocalDateTime modifiedTime;		// date of last modification
	
	@Column(insertable = false, updatable = false)
	private Integer modifiedById;			// user who last modified the element
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifiedById", nullable = true)
	private User modifiedBy;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "childElementId")
	private List<Association> parents;		// parents of the element
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentElementId")
	private List<Association> children;		// children of the element
	
	
	@Transient
	private UUID parentRef;
	
	
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
	}
	
	public void attachModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
		if(modifiedBy != null)
			this.modifiedById = modifiedBy.getId();
	}
}
