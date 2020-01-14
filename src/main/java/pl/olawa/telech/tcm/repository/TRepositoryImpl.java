package pl.olawa.telech.tcm.repository;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import pl.olawa.telech.tcm.model.entity.AbstractEntity;

/*
 * Baowa implementacja Repozytorium dla wszystkich klas. Dodaje metody pomocne przy używaniu specyfikacji i grafów encyjnych.
 */
public class TRepositoryImpl<T extends AbstractEntity> extends SimpleJpaRepository<T, Long> implements TRepository<T> {

	private EntityManager entityManager;


	public TRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager){
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T findOne(Specification<T> ...spec) {
        return findOne(null, spec);
    }
	
	@Override
	@SuppressWarnings("unchecked")
	public T findOne(String entityGraphName, Specification<T> ...spec) {
        TypedQuery<T> query = getQuery(conjunction(spec), Sort.unsorted());
        if(entityGraphName != null)
        	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
        
        return query.getSingleResult();
    }
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(Specification<T> ...spec) {
	    return findAll(null, null, spec);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(Sort sort, Specification<T> ...spec) {
		 return findAll(null, sort, spec);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(String entityGraphName, Specification<T> ...spec) {
		 return findAll(entityGraphName, null, spec);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(String entityGraphName, Sort sort, Specification<T> ...spec) {
	    TypedQuery<T> query = getQuery(conjunction(spec), (sort != null ? sort : Sort.unsorted()));
	    if(entityGraphName != null)
	    	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
	    
	    return query.getResultList();
	}
	
	@Override
	public T saveAndReload(T entity){
		entity = saveAndFlush(entity);
		entityManager.refresh(entity);
		return entity;
	}
	
	@SafeVarargs
	private final Specification<T> conjunction(Specification<T> ...specs){
		return Arrays.stream(specs)
			.filter(s -> s != null)
			.reduce(Specification::and)
			.orElse(null);
	}
	
}
