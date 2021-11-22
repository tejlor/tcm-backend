package pl.olawa.telech.tcm.repo.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.Join;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.repo.model.entity.assoc.Association;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.element.Element.Fields;


public interface ElementDAO extends DAO<Element>, JpaSpecificationExecutor<Element> {

	Element findByRef(UUID ref);

	default List<Element> findChildren(Integer parentId){
		return findAll(
				isChildOf(parentId)
				);
	}
	
	default List<Element> findParents(Integer childId){
		return findAll(
				isParentOf(childId)
				);
	}
	
	@SuppressWarnings("unchecked")
	default Pair<List<Element>, Integer> findChildren(Integer parentId, TableParams tableParams){
		return findAllWithCount(
				null,
				tableParams.getPage(),
				isChildOf(parentId),
				tableParams.getFilter() != null ? isLike(tableParams.getFilter()) : null
				);
	}

	// ######################### Specifications #########################################################################################################

	default Specification<Element> isChildOf(Integer parentId){
        return (element, cq, cb) -> {
        	Join<Element, Association> assoc = element.join(Fields.parentsAssoc);
            return cb.equal(assoc.get(Association.PROP_PARENT_ELEMENT_ID), parentId);
        };
	}
	
	default Specification<Element> isParentOf(Integer childId){
        return (element, cq, cb) -> {
        	Join<Element, Association> assoc = element.join(Fields.childrenAssoc);
            return cb.equal(assoc.get(Association.PROP_CHILD_ELEMENT_ID), childId);
        };
	}
	
	default Specification<Element> isLike(String filter){
        return (element, cq, cb) -> {
        	return cb.like(cb.lower(element.get(Fields.name)), "%" + filter + "%");
        };
	}
}

