package pl.olawa.telech.tcm.adm.logic.interfaces;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import pl.olawa.telech.tcm.adm.model.entity.UserGroup;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;

public interface UserGroupLogic {

	Pair<List<UserGroup>, Integer> loadTable(TableParams tableParams);

	UserGroup create(UserGroup user);

	UserGroup update(int id, UserGroup user);

	ByteArrayOutputStream exportToXlsx();

}
