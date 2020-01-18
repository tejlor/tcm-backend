package pl.olawa.telech.tcm.logic;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.model.entity.Element;
import pl.olawa.telech.tcm.repository.ElementRepository;

@Slf4j
@Service
@Transactional
public class ElementLogic extends AbstractLogic<Element> {

	
	public ElementLogic(ElementRepository repository) {
		super(repository);
	}
	


}
