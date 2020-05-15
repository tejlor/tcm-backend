package pl.olawa.telech.tcm.logic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.dao.ElementDAO;
import pl.olawa.telech.tcm.model.entity.element.Element;
import pl.olawa.telech.tcm.model.shared.Path;
import pl.olawa.telech.tcm.model.shared.TableParams;
import pl.olawa.telech.tcm.utils.TConstants;


@Slf4j
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
	
	public List<Element> loadChildren(UUID ref){
		Integer parentId = dao.findByRef(ref).getId();
		return dao.findChildren(parentId);
	}
	
	public Element loadParentsTree(UUID ref){
		Element element = dao.findByRef(ref);
		element.setChildrenElements(dao.findChildren(element.getId()));
		while(!element.getRef().equals(TConstants.ROOT_UUID)) {			
			element = loadParentWithOtherChildren(element);
		} 
				
		return element;
	}
	
	public Pair<List<Element>, Integer> loadChildren(UUID ref, TableParams tableParams){
		Integer parentId = dao.findByRef(ref).getId();
		return dao.findChildren(parentId, tableParams);
	}
	
	public Path loadAbsolutePath(UUID ref) {
		Element element = dao.findByRef(ref);
	
		StringBuilder refs = new StringBuilder(element.getRef().toString());
		StringBuilder names = new StringBuilder(element.getName());
		
		List<Element> parents =  dao.findParents(element.getId());
		while(parents.size() > 0) {
			Element parent = parents.get(0);
			refs.insert(0, parent.getRef().toString() + TConstants.DIRECTORY_SEPARATOR);
			names.insert(0, parent.getName() + TConstants.DIRECTORY_SEPARATOR);
			parents =  dao.findParents(parent.getId());
		}
		
		return new Path(refs.toString(), names.toString());
	}
	
	public void fillNew(Element element) {
		element.setRef(UUID.randomUUID());
		element.setCreatedTime(LocalDateTime.now());
		element.attachCreatedBy(accountLogic.loadCurrentUser());
	}
	
	// ################################### PRIVATE #########################################################################3

	private Element loadParentWithOtherChildren(Element child){
		Element parent = dao.findParents(child.getId()).get(0);
		List<Element> children = dao.findChildren(parent.getId()).stream()
				.filter(e -> !e.equals(child))
				.collect(Collectors.toList());
		children.add(child);
		parent.setChildrenElements(children);
		return parent;
	}
	
}
