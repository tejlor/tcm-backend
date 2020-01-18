package pl.olawa.telech.tcm.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/*
 * Interfejs dla bazowej implementacji Repozytorium.
 */
@NoRepositoryBean
public interface TRepository<T> extends JpaRepository<T, Integer> {

	@SuppressWarnings("unchecked")
	T findOne(Specification<T> ...spec);
	
	@SuppressWarnings("unchecked")
	T findOne(String entityGraphName, Specification<T> ...spec);

	@SuppressWarnings("unchecked")
	List<T> findAll(Specification<T> ...spec);
	
	@SuppressWarnings("unchecked")
	List<T> findAll(String entityGraphName, Specification<T> ...spec);

	@SuppressWarnings("unchecked")
	List<T> findAll(Sort sort, Specification<T> ...spec);

	@SuppressWarnings("unchecked")
	List<T> findAll(String entityGraphName, Sort sort, Specification<T> ...spec);

	T saveAndReload(T entity);

}
