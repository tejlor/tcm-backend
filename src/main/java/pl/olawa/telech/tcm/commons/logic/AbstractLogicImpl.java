package pl.olawa.telech.tcm.commons.logic;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.logic.interfaces.AccountLogic;
import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.commons.logic.interfaces.AbstractLogic;
import pl.olawa.telech.tcm.commons.model.entity.AbstractCreatableEntity;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.commons.model.entity.AbstractModifiableEntity;

@FieldDefaults(level = PROTECTED)
public abstract class AbstractLogicImpl<T extends AbstractEntity> implements AbstractLogic<T> {

	DAO<T> dao;
	
	@Autowired
	AccountLogic accountLogic;
	
		
	public AbstractLogicImpl(DAO<T> dao){
		this.dao = dao;
	}

	@Override
	public T loadById(Integer id) {
		if(id == null)
			throw new NullPointerException();
		
		return dao.findById(id).orElse(null);
	}

	@Override
	public List<T> loadAll() {
		return dao.findAll();
	}
	
	@Override
	public List<T> loadAllById() {
		return dao.findAllById();
	}
	
	protected <M extends AbstractCreatableEntity> void fillCreatable(M entity) {
		entity.setCreatedTime(LocalDateTime.now());
		entity.setCreatedBy(accountLogic.getCurrentUser());
	}
	
	protected <M extends AbstractModifiableEntity> void fillModifiable(M entity) {
		entity.setModifiedTime(LocalDateTime.now());
		entity.setModifiedBy(accountLogic.getCurrentUser());
	}
	
	protected T save(T entity){
		return dao.save(entity);
	}

	protected void delete(T entity) {
		dao.delete(entity);
	}
}
