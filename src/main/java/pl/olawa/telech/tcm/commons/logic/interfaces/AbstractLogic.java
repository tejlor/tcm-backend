package pl.olawa.telech.tcm.commons.logic.interfaces;

import java.util.List;

import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;

public interface AbstractLogic<T extends AbstractEntity> {
	T loadById(Integer id);
	List<T> loadAll();
	List<T> loadAllOrderById();
	T loadOrCreate(Integer id);
}
