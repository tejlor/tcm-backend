package pl.olawa.telech.tcm.commons.dao;

import static lombok.AccessLevel.PRIVATE;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.commons.model.entity.AbstractEntity;

/*
 * Base implementation of all repository classes. Adds some methods helpful in using specifications and entity graphs. 
 */
@FieldDefaults(level = PRIVATE)
public class DAOImpl<T extends AbstractEntity> extends SimpleJpaRepository<T, Integer> implements DAO<T> {

	EntityManager entityManager;	

	public DAOImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager){
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
        addEntityGraph(query, entityGraphName);
        return query.getSingleResult();
    }
	
	@Override
	public List<T> findAllOrderById() {
		return getQuery(null, Sort.by("id")).getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(Specification<T> ...spec) {
	    return findAll(null, Sort.unsorted(), spec);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(String entityGraphName, Specification<T> ...spec) {
		 return findAll(entityGraphName, Sort.unsorted(), spec);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(Sort sort, Specification<T> ...spec) {
		 return findAll(null, sort, spec);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(String entityGraphName, Sort sort, Specification<T> ...spec) {
		TypedQuery<T> query = getQuery(conjunction(spec), sort);
		addEntityGraph(query, entityGraphName);
	    return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Pair<List<T>, Integer> findAllWithCount(Pageable page, Specification<T> ...spec) {
		 return findAllWithCount(null, page, spec);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Pair<List<T>, Integer> findAllWithCount(String entityGraphName, Pageable page, Specification<T> ...spec) {
	    Specification<T> specSum = conjunction(spec);
		TypedQuery<T> query = getQuery(specSum, page);
	    addEntityGraph(query, entityGraphName);
	    query.setFirstResult((int) page.getOffset());
	    query.setMaxResults(page.getPageSize());
	    
	    return Pair.of(query.getResultList(), (int) count(specSum));
	}
	
	@Override
	public void detach(T entity) {
        entityManager.detach(entity);
    }
	
	// ################################### PRIVATE #########################################################################
	
	private void addEntityGraph(TypedQuery<T> query, String entityGraphName) {
		if(entityGraphName != null) {
		  	query.setHint(EntityGraphType.FETCH.getKey(), entityManager.getEntityGraph(entityGraphName));
		}
	}
	
	@SafeVarargs
	private Specification<T> conjunction(Specification<T> ...specs){
		return Arrays.stream(specs)
			.filter(s -> s != null)
			.reduce(Specification::and)
			.orElse(null);
	}
}
