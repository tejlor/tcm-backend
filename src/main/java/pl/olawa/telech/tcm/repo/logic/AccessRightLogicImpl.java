package pl.olawa.telech.tcm.repo.logic;

import static lombok.AccessLevel.PRIVATE;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;
import pl.olawa.telech.tcm.repo.dao.AccessRightDAO;
import pl.olawa.telech.tcm.repo.logic.interfaces.AccessRightLogic;
import pl.olawa.telech.tcm.repo.logic.interfaces.ElementLogic;
import pl.olawa.telech.tcm.repo.model.entity.AccessRight;
import pl.olawa.telech.tcm.repo.model.entity.element.Element;

@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class AccessRightLogicImpl extends AbstractLogicImpl<AccessRight> implements AccessRightLogic {

	AccessRightDAO dao;
	
	@Autowired
	ElementLogic elementLogic;
	
	
	public AccessRightLogicImpl(AccessRightDAO dao) {
		super(dao);
		this.dao = dao;
	}
	
	@Override
	public List<AccessRight> loadByElementRef(UUID elementRef){
		Element element = elementLogic.loadByRef(elementRef);
		return dao.findByElement(element.getId());
	}
		
	@Override
	public List<AccessRight> save(UUID elementRef, List<AccessRight> _accessRights) {
		Element element = elementLogic.loadByRef(elementRef);
		dao.deleteByElement(element.getId());
		
		int orderNo = 0;
		List<AccessRight> result = new ArrayList<>();
		for(AccessRight _accessRight : _accessRights) {
			var accessRight = new AccessRight();			
			accessRight.setElementId(element.getId());
			accessRight.setCanCreate(_accessRight.isCanCreate());
			accessRight.setCanRead(_accessRight.isCanRead());
			accessRight.setCanUpdate(_accessRight.isCanUpdate());
			accessRight.setCanDelete(_accessRight.isCanDelete());
			accessRight.setForAll(_accessRight.isForAll());
			accessRight.setUsers(_accessRight.getUsers());
			accessRight.setUserGroups(_accessRight.getUserGroups());
			if(accessRight.isValid()) {
				accessRight.setOrderNo(orderNo++);
				result.add(save(accessRight));
			}
		}
		return result;
	}
	
	// ################################### PRIVATE #########################################################################

}

