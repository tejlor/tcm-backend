package pl.olawa.telech.tcm.repo.logic;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.logic.interfaces.AccountLogic;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.repo.dao.ContainsAssocDAO;
import pl.olawa.telech.tcm.repo.logic.interfaces.ContainsAssocLogic;
import pl.olawa.telech.tcm.repo.model.entity.assoc.ContainsAssoc;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class ContainsAssocLogicImpl extends AbstractLogicImpl<ContainsAssoc> implements ContainsAssocLogic {

	@SuppressWarnings("unused")
	ContainsAssocDAO dao;
	
	@Autowired
	AccountLogic accountLogic;
	@Autowired
	ElementLogicImpl elementLogic;
	

	public ContainsAssocLogicImpl(ContainsAssocDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public void create(UUID parentRef, Element child) {
		Element parent = elementLogic.loadByRef(parentRef);	
		create(parent, child);
	}

	@Override
	public void create(Element parent, Element child) {
		var assoc = new ContainsAssoc();
		assoc.setParentElement(parent);
		assoc.setChildElement(child);
		assoc.setCreatedTime(LocalDateTime.now());
		assoc.setCreatedBy(accountLogic.getCurrentUser());
		save(assoc);
	}
	
	@Override
	public void deleteParentAssoc(Element element) {
		ContainsAssoc assoc = element.getParentsAssoc().iterator().next();
		delete(assoc);
	}
}
