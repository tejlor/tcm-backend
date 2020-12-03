package pl.olawa.telech.tcm.repo.logic;

import static lombok.AccessLevel.PRIVATE;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.repo.dao.FolderDAO;
import pl.olawa.telech.tcm.repo.model.entity.element.FolderEl;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class FolderLogic extends AbstractLogicImpl<FolderEl> {
	
	FolderDAO dao;
	
	@Autowired
	ContainsAssocLogic containsAssocLogic;
	@Autowired
	ElementLogic elementLogic;
	
	
	public FolderLogic(FolderDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	public FolderEl loadByRef(UUID ref) {
		return dao.findByRef(ref);
	}
	
	public FolderEl create(FolderEl folder) {
		elementLogic.fillNew(folder);
		folder = save(folder);
		
		containsAssocLogic.create(folder.getParentRef(), folder);
		
		return folder;
	}
	
	public void copy(FolderEl folder, FolderEl copy) {
		
	}
}
