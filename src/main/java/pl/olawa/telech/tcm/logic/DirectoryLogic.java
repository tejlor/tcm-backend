package pl.olawa.telech.tcm.logic;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.model.entity.Directory;
import pl.olawa.telech.tcm.repository.DirectoryRepository;

@Slf4j
@Service
@Transactional
public class DirectoryLogic extends AbstractLogic<Directory> {
	
	
	public DirectoryLogic(DirectoryRepository repository) {
		super(repository);
	}
	
	public Directory loadByRef(UUID ref) {
		repository.findByRef(ref);
	}


}
