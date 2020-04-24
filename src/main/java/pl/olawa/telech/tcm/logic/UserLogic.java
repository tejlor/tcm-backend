package pl.olawa.telech.tcm.logic;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.olawa.telech.tcm.dao.UserDAO;
import pl.olawa.telech.tcm.model.entity.User;


@Service
@Transactional
public class UserLogic extends AbstractLogic<User> {

	private UserDAO dao;
	
	public UserLogic(UserDAO dao) {
		super(dao);
		this.dao = dao;
	}

}
