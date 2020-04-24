package pl.olawa.telech.tcm.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.Join;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.assoc.Association;
import pl.olawa.telech.tcm.model.entity.element.Element;
import pl.olawa.telech.tcm.model.shared.TableParams;


public interface ElementDAO extends DAO<Element>, JpaSpecificationExecutor<Element> {

	Element findByRef(UUID ref);

	default List<Element> findByParent(Integer parentId){
		return findAll(
				isChildOf(parentId)
				);
	}
	
	@SuppressWarnings("unchecked")
	default Pair<List<Element>, Integer> findByParent(Integer parentId, TableParams tableParams){
		return findAllWithCount(
				null,
				tableParams.getPage(),
				isChildOf(parentId),
				tableParams.getFilter() != null ? isNameLike(tableParams.getFilter()) : null
				);
	}

	// ######################### Specifications #########################################################################################################

	default Specification<Element> isChildOf(Integer parentId){
        return (element, cq, cb) -> {
        	Join<Element, Association> assoc = element.join("parents");
            return cb.equal(assoc.get("parentElementId"), parentId);
        };
	}
	
	default Specification<Element> isNameLike(String filter){
        return (element, cq, cb) -> {
        	return cb.like(element.get("name"), "%" + filter + "%");
        };
	}
}

