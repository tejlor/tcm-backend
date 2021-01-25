package pl.olawa.telech.tcm.repo.logic;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.repo.dao.FolderDAO;
import pl.olawa.telech.tcm.repo.logic.interfaces.ContainsAssocLogic;
import pl.olawa.telech.tcm.repo.logic.interfaces.ElementLogic;
import pl.olawa.telech.tcm.repo.logic.interfaces.FolderLogic;
import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class FolderLogicImpl extends AbstractLogicImpl<FolderEl> implements FolderLogic {
	
	FolderDAO dao;
	
	@Autowired
	ContainsAssocLogic containsAssocLogic;
	@Autowired
	ElementLogic elementLogic;
	
	
	public FolderLogicImpl(FolderDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public FolderEl loadByRef(UUID ref) {
		return dao.findByRef(ref);
	}
	
	@Override
	public FolderEl create(UUID parentRef, FolderEl folder) {
		elementLogic.fillNew(folder);
		folder = save(folder);
		
		containsAssocLogic.create(parentRef, folder);
		
		return folder;
	}
	
	@Override
	public void copy(FolderEl folder, FolderEl copy) {
		List<UUID> refs = folder.getChildrenAssoc().stream()
			.map(a -> a.getChildElement().getRef())
			.collect(Collectors.toList());
		
		elementLogic.copy(refs, copy.getRef());
	}
}
