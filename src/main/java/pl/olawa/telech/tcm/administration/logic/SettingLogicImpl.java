package pl.olawa.telech.tcm.administration.logic;

import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.administration.dao.SettingDAO;
import pl.olawa.telech.tcm.administration.logic.interfaces.SettingLogic;
import pl.olawa.telech.tcm.administration.model.entity.Setting;
import pl.olawa.telech.tcm.commons.logic.AbstractLogicImpl;


@Slf4j
@Service
@Transactional
@FieldDefaults(level = PRIVATE)
public class SettingLogicImpl extends AbstractLogicImpl<Setting> implements SettingLogic {

	SettingDAO dao;
	
	public SettingLogicImpl(SettingDAO dao) {
		super(dao);
		this.dao = dao;
	}

	@Override
	public int loadIntValue(String name) {
		int value = 0;
		
		String valueStr = loadValue(name);
		if(valueStr != null) {
			try {
				value = Integer.parseInt(valueStr);
			}
			catch(NumberFormatException e) {
				log.warn("Cannot parse int setting value: " + name, e);
			}
		}
		
		return value;
	}
	
	@Override
	public BigDecimal loadDecimalValue(String name) {
		BigDecimal value = BigDecimal.ZERO;
		
		String valueStr = loadValue(name);
		if(valueStr != null) {
			try {
				value = new BigDecimal(valueStr);
			}
			catch(NumberFormatException e) {
				log.warn("Cannot parse decimal setting value: " + name, e);
			}
		}
		
		return value;
	}
	
	@Override
	public boolean loadBoolValue(String name) {
		boolean value = false;
		
		String valueStr = loadValue(name);
		if(valueStr != null) {
			value = Boolean.parseBoolean(valueStr);
		}
		
		return value;
	}
	
	@Override
	public UUID loadUUIDValue(String name) {
		UUID value = null;
		
		String valueStr = loadValue(name);
		if(valueStr != null) {
			try {
				value = UUID.fromString(valueStr);
			}
			catch(IllegalArgumentException e) {
				log.warn("Cannot parse uuid setting value: " + name, e);
			}
		}
		
		return value;
	}
	
	@Override
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
