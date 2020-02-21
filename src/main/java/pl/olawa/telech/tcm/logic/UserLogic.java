package pl.olawa.telech.tcm.logic;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.dao.UserDAO;
import pl.olawa.telech.tcm.model.entity.User;


@Service
@Transactional
public class UserLogic extends AbstractLogic<User> {


	public UserLogic(UserDAO dao) {
		super(dao);
	}

}
