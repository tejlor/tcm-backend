package pl.olawa.telech.tcm.commons.logic;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.commons.logic.interfaces.AbstractLogic;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;

@FieldDefaults(level = PRIVATE)
public abstract class AbstractLogicImpl<T extends AbstractEntity> implements AbstractLogic<T> {

	DAO<T> dao;
		
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
	
	protected T save(T entity){
		return dao.save(entity);
	}

	protected void delete(T entity) {
		dao.delete(entity);
	}
}
