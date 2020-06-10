package pl.olawa.telech.tcm.logic;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.olawa.telech.tcm.dao.FolderDAO;
import pl.olawa.telech.tcm.model.entity.element.FolderEl;

@Service
@Transactional
public class FolderLogic extends AbstractLogic<FolderEl> {
	
	private FolderDAO dao;
	
	@Autowired
	private ContainsAssocLogic containsAssocLogic;
	@Autowired
	private ElementLogic elementLogic;
	
	
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
	
	public void copy(FolderEl folder) {
		
	}

}
