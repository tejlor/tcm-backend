package pl.olawa.telech.tcm.adm.logic;

import static lombok.AccessLevel.PRIVATE;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.adm.dao.UserDAO;
import pl.olawa.telech.tcm.adm.logic.interfaces.AccountLogic;
import pl.olawa.telech.tcm.adm.logic.interfaces.UserLogic;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.commons.logic.helper.ExcelExporter;
import pl.olawa.telech.tcm.commons.model.exception.TcmException;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.commons.utils.TUtils;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class UserLogicImpl extends AbstractLogicImpl<User> implements UserLogic {

	UserDAO dao;
	
	@Autowired
	AccountLogic accountLogic;
	
	public UserLogicImpl(UserDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public Pair<List<User>, Integer> loadTable(TableParams tableParams){
		return dao.findAll(tableParams);
	} 
	
	@Override
	public User create(User _user) {
		validate(_user);
		
		var user = new User();
		user.setCreatedTime(LocalDateTime.now());
		user.setCreatedBy(accountLogic.loadCurrentUser());
		
		user.setFirstName(_user.getFirstName());
		user.setLastName(_user.getLastName());
		user.setEmail(_user.getEmail());
		user.setPassword(user.getEmail());
		user.setGroups(_user.getGroups());
		
		return save(user);
	}
	
	@Override
	public User update(int id, User _user) {
		validate(_user);
		
		User user = loadById(id);
		TUtils.assertEntityExists(user);

		user.setModifiedBy(accountLogic.getCurrentUser());
		user.setModifiedTime(LocalDateTime.now());
		
		user.setFirstName(_user.getFirstName());
		user.setLastName(_user.getLastName());
		user.setEmail(_user.getEmail());
		user.setGroups(_user.getGroups());

		return save(user);
	}
	
	@Override 
	public ByteArrayOutputStream exportToXlsx() {
		return new ExcelExporter<User>()
				.title("Users")
				.column(new ExcelExporter.Column<User>("Id", User::getId))
				.column(new ExcelExporter.Column<User>("First name", User::getFirstName))
				.column(new ExcelExporter.Column<User>("Last name", User::getLastName))
				.column(new ExcelExporter.Column<User>("E-mail", User::getEmail))
				.dataSet(loadAllById())
				.create();
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void validate(User user) {
		if(TUtils.isEmpty(user.getEmail()))
			throw new TcmException("E-mail address is required.");
	}

}
