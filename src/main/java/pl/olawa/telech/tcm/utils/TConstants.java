package pl.olawa.telech.tcm.utils;

import java.math.BigDecimal;
import java.util.UUID;

public class TConstants {

	public static final BigDecimal EIGHT = new BigDecimal(8);
	public static final BigDecimal FOURTY = new BigDecimal(40);
	public static final BigDecimal HUNDRED = new BigDecimal(100);
	
	public static final String[] INDENT = new String[]{"  ", "    ", "      ",  "        ",  "          "};
	
	public static final String ROOT_REF = "00000000-0000-0000-0000-000000000000";
	public static final UUID ROOT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	
	public static final String DIRECTORY_SEPARATOR = "/";
}
