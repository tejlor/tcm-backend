package pl.olawa.telech.tcm.repo.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

public enum FeatureAttributeType {

	INT, FLOAT, DEC,
	STRING, TEXT,
	BOOL,
	DATE, TIME;
	
	public String getTypedPropertyName(){
		return "value" + StringUtils.capitalize(this.name().toLowerCase());
	}
	
	public static List<String> getTypedPropertyNames(){
		return Arrays.stream(values())
				.map(v -> "value" + StringUtils.capitalize(v.name().toLowerCase()))
				.collect(Collectors.toList());
	}
}
