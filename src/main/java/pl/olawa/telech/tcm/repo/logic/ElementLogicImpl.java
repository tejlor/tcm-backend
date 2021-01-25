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
import pl.olawa.telech.tcm.adm.logic.interfaces.AccountLogic;
import pl.olawa.telech.tcm.adm.logic.interfaces.SettingLogic;
import pl.olawa.telech.tcm.adm.model.entity.Setting;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.commons.model.shared.Path;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.commons.utils.TConstants;
import pl.olawa.telech.tcm.repo.dao.ElementDAO;
import pl.olawa.telech.tcm.repo.logic.interfaces.ContainsAssocLogic;
import pl.olawa.telech.tcm.repo.logic.interfaces.ElementLogic;
import pl.olawa.telech.tcm.repo.logic.interfaces.FileLogic;
import pl.olawa.telech.tcm.repo.logic.interfaces.FolderLogic;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;
import pl.olawa.telech.tcm.repo.model.entity.element.FileEl;
import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;


@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class ElementLogicImpl extends AbstractLogicImpl<Element> implements ElementLogic {

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
	SettingLogic settingLogic;
	
	
	public ElementLogicImpl(ElementDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public Element loadByRef(UUID ref) {
		return dao.findByRef(ref);
	}
	
	@Override
	public List<Element> loadChildren(UUID ref){
		Integer parentId = dao.findByRef(ref).getId();
		return dao.findChildren(parentId);
	}
	
	@Override
	public Element loadParentsTree(UUID ref){
		UUID rootRef = getRootRef();
		Element element = dao.findByRef(ref);
		element.setChildrenElements(dao.findChildren(element.getId()));
		while(!element.getRef().equals(rootRef)) {			
			element = loadParentWithOtherChildren(element);
		} 				
		return element;
	}
	
	@Override
	public Pair<List<Element>, Integer> loadChildren(UUID ref, TableParams tableParams){
		Integer parentId = dao.findByRef(ref).getId();
		return dao.findChildren(parentId, tableParams);
	}
	
	@Override
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
	
	@Override
	public void rename(UUID ref, String newName) {
		Element element = dao.findByRef(ref);
		element.setName(newName);
		save(element);
	}
	
	@Override
	public void move(List<UUID> refs, UUID newParentRef) {
		for(UUID ref : refs) {
			Element element = dao.findByRef(ref);
			containsAssocLogic.deleteParentAssoc(element);
			containsAssocLogic.create(newParentRef, element);
		}
	}
	
	@Override
	public void copy(List<UUID> refs, UUID newParentRef) {
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

			containsAssocLogic.create(newParentRef, copy);
		}
	}
	
	@Override
	public void remove(List<UUID> refs) {
		UUID trashRef = settingLogic.loadUUIDValue(Setting.TRASH_REF);
		move(refs, trashRef);
	}
	
	@Override
	public void fillNew(Element element) {
		element.setRef(UUID.randomUUID());
		element.setCreatedTime(LocalDateTime.now());
		element.setCreatedBy(accountLogic.loadCurrentUser());
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
	
	private UUID getRootRef() {
		return settingLogic.loadUUIDValue(Setting.ROOT_REF);
	}
}
