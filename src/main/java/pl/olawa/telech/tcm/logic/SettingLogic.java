package pl.olawa.telech.tcm.logic;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.dao.SettingDAO;
import pl.olawa.telech.tcm.dao.UserDAO;
import pl.olawa.telech.tcm.model.entity.Setting;
import pl.olawa.telech.tcm.model.entity.User;


@Service
@Transactional
@Slf4j
public class SettingLogic extends AbstractLogic<Setting> {

	private SettingDAO dao;
	
	
	public SettingLogic(SettingDAO dao) {
		super(dao);
		this.dao = dao;
	}

	public int loadIntValue(String name) {
		int value = 0;
		
		String valueStr = loadValue(name);
		if(valueStr != null) {
			try {
				value = Integer.parseInt(valueStr);
			}
			catch(NumberFormatException e) {
				log.warn("Cannot parse setting value: " + name, e);
			}
		}
		
		return value;
	}
	
	public BigDecimal loadDecimalValue(String name) {
		BigDecimal value = BigDecimal.ZERO;
		
		String valueStr = loadValue(name);
		if(valueStr != null) {
			try {
				value = new BigDecimal(valueStr);
			}
			catch(NumberFormatException e) {
				log.warn("Cannot parse setting value: " + name, e);
			}
		}
		
		return value;
	}
	
	public boolean loadBoolValue(String name) {
		boolean value = false;
		
		String valueStr = loadValue(name);
		if(valueStr != null) {
			value = Boolean.parseBoolean(valueStr);
		}
		
		return value;
	}
	
	public UUID loadUUIDValue(String name) {
		UUID value = null;
		
		String valueStr = loadValue(name);
		if(valueStr != null) {
			try {
				value = UUID.fromString(valueStr);
			}
			catch(IllegalArgumentException e) {
				log.warn("Cannot parse setting value: " + name, e);
			}
		}
		
		return value;
	}
	
	public String loadStringValue(String name) {
		return loadValue(name);
	}
	
	// ################################### PRIVATE #########################################################################
	
	private String loadValue(String name) {
		Setting setting = dao.findByName(name);
		if(setting == null)
			return null;
		
		return setting.getValue();
	}

}
