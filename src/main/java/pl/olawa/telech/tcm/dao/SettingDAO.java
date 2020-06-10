package pl.olawa.telech.tcm.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.olawa.telech.tcm.model.entity.Setting;


public interface SettingDAO extends DAO<Setting>, JpaSpecificationExecutor<Setting> {

	Setting findByName(String name);

}

