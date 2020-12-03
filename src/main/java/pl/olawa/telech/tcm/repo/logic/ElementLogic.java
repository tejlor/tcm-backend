package pl.olawa.telech.tcm.repo.logic;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.administration.logic.SettingLogicImpl;
import pl.olawa.telech.tcm.administration.logic.interfaces.AccountLogic;
import pl.olawa.telech.tcm.administration.model.entity.Setting;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.commons.model.shared.Path;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.commons.utils.TConstants;
import pl.olawa.telech.tcm.repo.dao.ElementDAO;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;
import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;


@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class ElementLogic extends AbstractLogicImpl<Element> {

	ElementDAO dao;
	
	@Autowired
	AccountLogic accountLogic;
	@Autowired
	ContainsAssocLogic containsAssocLogic;
	@Autowired
	FileLogic fileLogic;
	@Autowired
	FolderLogic folderLogic;
	@Autowired
	SettingLogicImpl settingLogic;
	
	
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
	
	public void rename(UUID ref, String newName) {
		Element element = dao.findByRef(ref);
		element.setName(newName);
		save(element);
	}
	
	public void move(UUID newParentRef, List<UUID> refs) {
		for(UUID ref : refs) {
			Element element = dao.findByRef(ref);
			containsAssocLogic.delete(element);
			containsAssocLogic.create(newParentRef, element);
		}
	}
	
	public void copy(UUID newParentRef, List<UUID> refs) {
		for(UUID ref : refs) {
			Element element = dao.findByRef(ref);
			
			Element copy = element.copy();
			fillNew(copy);
			save(copy);
			
			if(element instanceof FileEl) {
				fileLogic.copy((FileEl) element, (FileEl) copy);
			}
			else if(element instanceof FolderEl) {
				folderLogic.copy((FolderEl) element, (FolderEl) copy);
			}

			containsAssocLogic.create(newParentRef, element);
		}
	}
	
	public void delete(List<UUID> refs) {
		UUID trashRef = settingLogic.loadUUIDValue(Setting.TRASH_REF);
		move(trashRef, refs);
	}
	
	public void fillNew(Element element) {
		element.setRef(UUID.randomUUID());
		element.setCreatedTime(LocalDateTime.now());
		element.attachCreatedBy(accountLogic.loadCurrentUser());
	}
	
	// ################################### PRIVATE #########################################################################

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
