package pl.olawa.telech.tcm.commons.logic;

import static lombok.AccessLevel.PROTECTED;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
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

	public AbstractLogicImpl(DAO<T> dao) {
		this.dao = dao;
	}

	@Override
	public T loadById(Integer id) {
		if (id == null)
			throw new NullPointerException();

		return dao.findById(id).orElse(null);
	}

	@Override
	public T loadOrCreate(Integer id) {
		if (id == null) {
			try {
				Constructor<T> cons = getGenericType().getDeclaredConstructor();
				return cons.newInstance();
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} 
		else {
			return loadById(id);
		}
	}

	@Override
	public List<T> loadAll() {
		return dao.findAll();
	}

	@Override
	public List<T> loadAllOrderById() {
		return dao.findAllOrderById();
	}

	protected void fillAuditData(T entity) {
		if(entity instanceof AbstractCreatableEntity && entity.getId() == null) {
			fillCreatable((AbstractCreatableEntity) entity);
		}
		else if(entity instanceof AbstractModifiableEntity && entity.getId() != null) {
			fillModifiable((AbstractModifiableEntity) entity);
		}
	}
	
	private void fillCreatable(AbstractCreatableEntity entity) {
		entity.setCreatedTime(LocalDateTime.now());
		entity.setCreatedBy(accountLogic.getCurrentUser());
	}

	private void fillModifiable(AbstractModifiableEntity entity) {
		entity.setModifiedTime(LocalDateTime.now());
		entity.setModifiedBy(accountLogic.getCurrentUser());
	}

	protected T save(T entity) {
		return dao.save(entity);
	}

	protected void delete(T entity) {
		dao.delete(entity);
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> getGenericType() {
		return (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
