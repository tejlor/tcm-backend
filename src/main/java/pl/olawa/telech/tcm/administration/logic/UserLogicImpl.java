package pl.olawa.telech.tcm.administration.logic;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.administration.dao.UserDAO;
import pl.olawa.telech.tcm.administration.logic.interfaces.UserLogic;
import pl.olawa.telech.tcm.administration.model.entity.User;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;


@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class UserLogicImpl extends AbstractLogicImpl<User> implements UserLogic {

	@SuppressWarnings("unused")
	UserDAO dao;
	
	public UserLogicImpl(UserDAO dao) {
		super(dao);
		this.dao = dao;
	}
}
