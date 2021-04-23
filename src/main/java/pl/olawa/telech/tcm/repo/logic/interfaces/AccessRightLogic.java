package pl.olawa.telech.tcm.repo.logic.interfaces;

import java.util.List;
import java.util.UUID;

import pl.olawa.telech.tcm.commons.logic.interfaces.AbstractLogic;
import pl.olawa.telech.tcm.repo.model.entity.AccessRight;

public interface AccessRightLogic extends AbstractLogic<AccessRight> {

	List<AccessRight> save(UUID elementRef, List<AccessRight> _accessRights);

	List<AccessRight> loadByElementRef(UUID elementRef);

}
