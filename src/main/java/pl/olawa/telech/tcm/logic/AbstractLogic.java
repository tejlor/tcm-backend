package pl.olawa.telech.tcm.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import pl.olawa.telech.tcm.dao.DAO;
import pl.olawa.telech.tcm.model.entity.AbstractEntity;


public abstract class AbstractLogic<T extends AbstractEntity> {

	protected DAO<T> dao;
	
	@Autowired
	protected AccountLogic accountLogic;
	
		
	protected AbstractLogic(DAO<T> dao){
		this.dao = dao;
	}

	public T loadById(Integer id) {
		if(id == null)
			throw new NullPointerException();
		
		return dao.findById(id).orElse(null);
	}

	public List<T> loadAll() {
		return dao.findAll();
	}
	
	public T save(T entity){
		return dao.save(entity);
	}

}
