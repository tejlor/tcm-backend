package pl.olawa.telech.tcm.commons.model.exception;


public class NotFoundException extends RuntimeException {

	public NotFoundException(){
		super();
	}
		
	public String toShortString(){
		return "404 NotFound";
	}
}
