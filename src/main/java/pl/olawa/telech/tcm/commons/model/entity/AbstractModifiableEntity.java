package pl.olawa.telech.tcm.commons.model.entity;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import pl.olawa.telech.tcm.adm.model.entity.User;

/*
 * Base class for enity classes with columns modifiedTime and modifiedBy.
 */
@Getter
@MappedSuperclass
@NoArgsConstructor
@FieldNameConstants
@FieldDefaults(level = PRIVATE)
public abstract class AbstractModifiableEntity extends AbstractCreatableEntity {

	@Column(insertable = false, updatable = false)
	Integer modifiedById;				// user's id modified the record last time
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifiedById", nullable = false)
	User modifiedBy;					// user modified the record last time
	
	@Setter
	@Column
	LocalDateTime modifiedTime;			// record last modified date and time
	
	public AbstractModifiableEntity(Integer id) {
		super(id);
	}
	
	public void setModifiedBy(User user) {
		modifiedBy = user;
		modifiedById = user != null ? user.getId() : null;
	}
}
