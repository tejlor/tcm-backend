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
import pl.olawa.telech.tcm.adm.dao.UserGroupDAO;
import pl.olawa.telech.tcm.adm.logic.interfaces.AccountLogic;
import pl.olawa.telech.tcm.adm.logic.interfaces.UserGroupLogic;
import pl.olawa.telech.tcm.adm.model.entity.UserGroup;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.commons.logic.helper.ExcelExporter;
import pl.olawa.telech.tcm.commons.model.exception.TcmException;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;
import pl.olawa.telech.tcm.commons.utils.TUtils;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class UserGroupLogicImpl extends AbstractLogicImpl<UserGroup> implements UserGroupLogic {

	UserGroupDAO dao;
	
	@Autowired
	AccountLogic accountLogic;
	
	public UserGroupLogicImpl(UserGroupDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public Pair<List<UserGroup>, Integer> loadTable(TableParams tableParams){
		return dao.findAll(tableParams);
	} 
	
	@Override
	public UserGroup create(UserGroup _userGroup) {
		validate(_userGroup);
		
		var userGroup = new UserGroup();
		userGroup.setCreatedTime(LocalDateTime.now());
		userGroup.setCreatedBy(accountLogic.loadCurrentUser());
		
		userGroup.setName(_userGroup.getName());
		
		return save(userGroup);
	}
	
	@Override
	public UserGroup update(int id, UserGroup _userGroup) {
		validate(_userGroup);
		
		UserGroup userGroup = loadById(id);
		TUtils.assertEntityExists(userGroup);
		
		userGroup.setName(_userGroup.getName());
		
		userGroup.setModifiedBy(accountLogic.getCurrentUser());
		userGroup.setModifiedTime(LocalDateTime.now());
		
		return save(userGroup);
	}
	
	@Override 
	public ByteArrayOutputStream exportToXlsx() {
		return new ExcelExporter<UserGroup>()
				.title("User Groups")
				.column(new ExcelExporter.Column<UserGroup>("Id", UserGroup::getId))
				.column(new ExcelExporter.Column<UserGroup>("Name", UserGroup::getName))
				.dataSet(loadAllOrderById())
				.create();
	}
	
	// ################################### PRIVATE #########################################################################
	
	private void validate(UserGroup user) {
		if(TUtils.isEmpty(user.getName()))
			throw new TcmException("Name is required.");
	}

}
