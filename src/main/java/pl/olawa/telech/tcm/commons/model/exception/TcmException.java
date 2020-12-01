package pl.olawa.telech.tcm.commons.model.exception;

import java.util.Arrays;
import java.util.stream.Collectors;


public class TcmException extends RuntimeException {

	public TcmException(){
		super();
	}
	
	public TcmException(String msg){
		super(msg);
	}
	
	public TcmException(String msg, Throwable cause){
		super(msg, cause);
	}
	
	public String toShortString(){
		return Arrays.stream(getStackTrace())
			.limit(5)
			.map(st -> st.toString())
			.collect(Collectors.joining("\n  ", getMessage() + "\n  ", "\n  ..."));
	}
}
