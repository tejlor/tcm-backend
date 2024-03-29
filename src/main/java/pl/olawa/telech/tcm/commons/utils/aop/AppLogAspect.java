package pl.olawa.telech.tcm.commons.utils.aop;

import java.io.IOException;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.adm.model.entity.User;
import pl.olawa.telech.tcm.commons.config.filter.RequestWrapper;
import pl.olawa.telech.tcm.commons.model.exception.NotFoundException;
import pl.olawa.telech.tcm.commons.model.exception.TcmException;
import pl.olawa.telech.tcm.commons.model.interfaces.Loggable;

/*
 * Aspect logging every request.
 */
@Slf4j
@Aspect
@Component
public class AppLogAspect {

	private static final long MEGABYTE = 1024 * 1024;
	
	@Pointcut("execution(public * pl.olawa.telech.tcm.*.controller.*.*(..))")
	public void controllerPublicMethods() {
	}
	
	@Pointcut("@annotation(pl.olawa.telech.tcm.commons.utils.aop.AppLogOmit)")
	public void isAppLogOmit() {}


	@Around("controllerPublicMethods() && !isAppLogOmit()")
	public Object logRequest(ProceedingJoinPoint jp) throws Throwable {	
		long startTime = System.currentTimeMillis();
		
		StringBuilder sb = new StringBuilder();
		sb.append(getRequestData());
		
		Object result = null;
		try {
			result = jp.proceed();
			sb.append(getResultData(result));	
		} 
		catch(NotFoundException e){
			sb.append("\n").append(e.toShortString());
			throw e;
		}
		catch(TcmException e) {			
			sb.append("\n").append(e.toShortString());
			throw e;
		}
		catch(Throwable e){
			sb.append("\nERROR:");
			throw e;
		}
		finally {
			sb.append("\nTIME: " + (System.currentTimeMillis() - startTime) + " ms");
			sb.append("\nMEMORY: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/MEGABYTE + " MB\n");
			log.trace(sb.toString());
		}

		return result;
	}
	
	private String getRequestData() throws IOException{
		String userName = getUserFromContext();
		RequestWrapper request = getRequestFromContext(); 
		if(request == null)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(userName).append("] ")
			.append(request.getMethod()).append(" ").append(request.getRequestURI());
		
		if(request.getQueryString() != null)
			sb.append("?").append(request.getQueryString());
					
		if (request.getMethod().equalsIgnoreCase("POST") || request.getMethod().equalsIgnoreCase("PUT"))
			sb.append("\nBODY:\n").append(request.getBody());
		
		return sb.toString();
	}
	
	private String getResultData(Object result){
		if(result == null)
			return "";
		
		StringBuilder sb = new StringBuilder("\nRESPONSE:\n");			
		if(result instanceof Loggable){
			sb.append(((Loggable) result).toFullString(0));
		}
		else if (result instanceof List){
			List<?> list = (List<?>) result;
			if(list.size() > 0){
				sb.append("  List of ").append(list.size()).append(" elements, first:\n");
				Object firstEl = list.get(0);
				if(firstEl instanceof Loggable){
					sb.append(((Loggable) firstEl).toFullString(1));
				}
				else {
					sb.append(firstEl.toString());
				}
			}
			else {
				sb.append("Empty List");
			}
		}
		else {
			sb.append(result.toString());
		}
		
		return sb.toString();
	}
	
	private String getUserFromContext() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Object principal = authentication.getPrincipal();
			if (principal instanceof User)
				return ((User) principal).calcShortName();
			else
				return "?";
		}
		catch (Exception e) {
			log.warn("Principal is null: " + e.getMessage());
			return "?";
		}
	}
	
	private RequestWrapper getRequestFromContext(){
		ServletRequestAttributes requestAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		var request = requestAttr.getRequest();
		return request instanceof RequestWrapper ? (RequestWrapper) requestAttr.getRequest() : null;
	}
}
