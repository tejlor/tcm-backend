package pl.olawa.telech.tcm.logic;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.model.entity.element.Directory;
import pl.olawa.telech.tcm.repository.DirectoryRepository;

@Slf4j
@Service
@Transactional
public class DirectoryLogic extends AbstractLogic<Directory> {
	
	private DirectoryRepository repository;
	
	
	public DirectoryLogic(DirectoryRepository repository) {
		super(repository);
		this.repository = repository;
	}
	
	public Directory loadByRef(UUID ref) {
		return repository.findByRef(ref);
	}


}
