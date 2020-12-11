package pl.olawa.telech.tcm.adm.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.adm.model.entity.Setting;
import pl.olawa.telech.tcm.commons.dao.interfaces.DAO;


public interface SettingDAO extends DAO<Setting>, JpaSpecificationExecutor<Setting> {

	Setting findByName(String name);
}
