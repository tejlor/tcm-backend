package pl.olawa.telech.tcm.adm.logic.interfaces;

import java.math.BigDecimal;
import java.util.UUID;

public interface SettingLogic {

	int loadIntValue(String name);
	BigDecimal loadDecimalValue(String name);
	boolean loadBoolValue(String name);
	UUID loadUUIDValue(String name);
	String loadStringValue(String name);
}
