package pl.olawa.telech.tcm.logic;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.olawa.telech.tcm.dao.ContainsAssocDAO;
import pl.olawa.telech.tcm.model.entity.assoc.ContainsAssoc;
import pl.olawa.telech.tcm.model.entity.element.Folder;
import pl.olawa.telech.tcm.model.entity.element.Element;

@Service
@Transactional
public class ContainsAssocLogic extends AbstractLogic<ContainsAssoc> {

	private ContainsAssocDAO dao;
	
	@Autowired
	private FolderLogic directoryLogic;
	

	public ContainsAssocLogic(ContainsAssocDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	public void create(UUID parentRef, Element child) {
		Folder parentDir = directoryLogic.loadByRef(parentRef);	
		create(parentDir, child);
	}

	public void create(Element parent, Element child) {
		ContainsAssoc assoc = new ContainsAssoc();
		assoc.setParentElement(parent);
		assoc.setChildElement(child);
		assoc.setCreatedTime(LocalDateTime.now());
		assoc.setCreatedBy(accountLogic.getCurrentUser());
		save(assoc);
	}
}
