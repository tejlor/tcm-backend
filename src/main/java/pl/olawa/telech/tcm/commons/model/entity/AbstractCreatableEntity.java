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
import pl.olawa.telech.tcm.adm.model.entity.User;

/*
 * Base class for enity classes with columns createdTime and createdBy.
 */
@Getter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@MappedSuperclass
public abstract class AbstractCreatableEntity extends AbstractEntity {

	@Column(nullable = false, insertable = false, updatable = false)
	Integer createdById;			// user's id created the record
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdById")
	User createdBy;					// user created the record
	
	@Setter
	@Column(nullable = false)
	LocalDateTime createdTime;		// record creation date and time
	
	public AbstractCreatableEntity(Integer id) {
		super(id);
	}
	
	public void setCreatedBy(User user) {
		createdBy = user;
		createdById = user != null ? user.getId() : null;
	}
}
