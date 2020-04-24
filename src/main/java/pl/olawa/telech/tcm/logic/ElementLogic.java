package pl.olawa.telech.tcm.logic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.olawa.telech.tcm.dao.ElementDAO;
import pl.olawa.telech.tcm.model.entity.element.Element;
import pl.olawa.telech.tcm.model.shared.TableParams;


@Service
@Transactional
public class ElementLogic extends AbstractLogic<Element> {

	private ElementDAO dao;
	
	
	public ElementLogic(ElementDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	public Element loadByRef(UUID ref) {
		return dao.findByRef(ref);
	}
	
	public List<Element> loadByParent(UUID ref){
		Integer parentId = dao.findByRef(ref).getId();
		return dao.findByParent(parentId);
	}
	
	public Pair<List<Element>, Integer> loadByParent(UUID ref, TableParams tableParams){
		Integer parentId = dao.findByRef(ref).getId();
		return dao.findByParent(parentId, tableParams);
	}
	
	public void fillNew(Element element) {
		element.setRef(UUID.randomUUID());
		element.setCreatedTime(LocalDateTime.now());
		element.attachCreatedBy(accountLogic.loadCurrentUser());
	}

}
