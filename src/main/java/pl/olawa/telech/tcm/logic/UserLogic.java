package pl.olawa.telech.tcm.logic;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.model.entity.User;
import pl.olawa.telech.tcm.repository.UserRepository;


@Slf4j
@Service
@Transactional
public class UserLogic extends AbstractLogic<User> {


	public UserLogic(UserRepository userRepository) {
		super(userRepository);
	}
	


}
