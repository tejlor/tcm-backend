package pl.olawa.telech.tcm.logic;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.model.entity.element.Element;
import pl.olawa.telech.tcm.model.entity.assoc.ContainsAssoc;
import pl.olawa.telech.tcm.repository.ContainsAssocRepository;

@Slf4j
@Service
@Transactional
public class ContainsAssocLogic extends AbstractLogic<ContainsAssoc> {


	public ContainsAssocLogic(ContainsAssocRepository repository) {
		super(repository);
	}
	
	public void create(Element parent, Element child) {
		ContainsAssoc assoc = new ContainsAssoc();
		assoc.setParentElement(parent);
		assoc.setChildElement(child);
		repository.save(assoc);
	}

}
