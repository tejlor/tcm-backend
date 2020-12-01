package pl.olawa.telech.tcm.repo.logic;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.olawa.telech.tcm.administration.logic.interfaces.AccountLogic;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.repo.dao.ContainsAssocDAO;
import pl.olawa.telech.tcm.repo.model.entity.assoc.ContainsAssoc;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

@Service
@Transactional
public class ContainsAssocLogic extends AbstractLogicImpl<ContainsAssoc> {

	private ContainsAssocDAO dao;
	
	@Autowired
	private AccountLogic accountLogic;
	@Autowired
	private ElementLogic elementLogic;
	

	public ContainsAssocLogic(ContainsAssocDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	public void create(UUID parentRef, Element child) {
		Element parent = elementLogic.loadByRef(parentRef);	
		create(parent, child);
	}

	public void create(Element parent, Element child) {
		ContainsAssoc assoc = new ContainsAssoc();
		assoc.setParentElement(parent);
		assoc.setChildElement(child);
		assoc.setCreatedTime(LocalDateTime.now());
		assoc.setCreatedBy(accountLogic.getCurrentUser());
		save(assoc);
	}
	
	public void delete(Element element) {
		ContainsAssoc assoc = element.getParents().get(0);
		delete(assoc);
	}
}
