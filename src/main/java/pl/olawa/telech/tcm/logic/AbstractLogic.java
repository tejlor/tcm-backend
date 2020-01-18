package pl.olawa.telech.tcm.logic;

import java.util.List;

import pl.olawa.telech.tcm.model.entity.AbstractEntity;
import pl.olawa.telech.tcm.repository.TRepository;


public abstract class AbstractLogic<T extends AbstractEntity> {

	protected TRepository<T> repository;
	
		
	protected AbstractLogic(TRepository<T> repository){
		this.repository = repository;
	}

	public T loadById(Integer id) {
		if(id == null)
			throw new NullPointerException();
		
		return repository.findById(id).orElse(null);
	}

	public List<T> loadAll() {
		return repository.findAll();
	}
	
	public T save(T entity){
		return repository.save(entity);
	}

}
