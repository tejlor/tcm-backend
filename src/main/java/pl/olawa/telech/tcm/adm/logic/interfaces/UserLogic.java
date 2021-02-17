package pl.olawa.telech.tcm.adm.logic.interfaces;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.commons.model.shared.TableParams;

public interface UserLogic {

	Pair<List<User>, Integer> loadTable(TableParams tableParams);

	User create(User user);

	User update(int id, User user);

	ByteArrayOutputStream exportToXlsx();

}
