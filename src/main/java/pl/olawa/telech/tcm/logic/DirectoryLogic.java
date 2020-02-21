package pl.olawa.telech.tcm.logic;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.dao.DirectoryDAO;
import pl.olawa.telech.tcm.model.entity.element.Directory;

@Slf4j
@Service
@Transactional
public class DirectoryLogic extends AbstractLogic<Directory> {
	
	private DirectoryDAO dao;
	
	@Autowired
	private ContainsAssocLogic containsAssocLogic;
	@Autowired
	private ElementLogic elementLogic;
	
	
	public DirectoryLogic(DirectoryDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	public Directory loadByRef(UUID ref) {
		return dao.findByRef(ref);
	}
	
	public Directory create(Directory dir) {
		elementLogic.fillNew(dir);
		dir = save(dir);
		
		containsAssocLogic.create(dir.getParentRef(), dir);
		
		return dir;
	}

}
