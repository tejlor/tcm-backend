package pl.olawa.telech.tcm.commons.controller;

import static lombok.AccessLevel.PRIVATE;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import pl.olawa.telech.tcm.commons.model.exception.NotFoundException;
import pl.olawa.telech.tcm.commons.model.exception.TcmException;
import pl.olawa.telech.tcm.commons.model.shared.ApiError;
import pl.olawa.telech.tcm.commons.utils.TUtils;
import pl.olawa.telech.tcm.commons.utils.aop.AppLogOmit;

@Slf4j
@RestController
@FieldDefaults(level = PRIVATE)
public class AbstractController {
	
	@Value("${tcm.environment}")
	String environment;

	protected static final String ID = "[0-9]+";
	protected static final String REF = "[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}";
	protected static final String CODE = "[a-zA-Z_\\-]+";
	
	/*
	 * Configuration needed for using LocalDate class in arguments of constructor methods without @DateTimeFormat annotation.
	 */
	@AppLogOmit
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(LocalDate.parse(text, DateTimeFormatter.ISO_DATE));
			}
		});
		
		binder.registerCustomEditor(UUID.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(UUID.fromString(text));
			}
		});
	}

	@AppLogOmit
	@ExceptionHandler(TcmException.class)
	public ResponseEntity<ApiError> handleTcmException(HttpServletRequest request, TcmException e) {
		ApiError apiError = new ApiError();
		apiError.setPath(request.getRequestURI());
		apiError.setStatusCode(HttpStatus.BAD_REQUEST);
		apiError.setErrorMessage(e.getMessage());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);
	}

	@AppLogOmit
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiError> handleNotFoundException(HttpServletRequest request, NotFoundException e) {
		ApiError apiError = new ApiError();
		apiError.setPath(request.getRequestURI());
		apiError.setStatusCode(HttpStatus.NOT_FOUND);
		return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);
	}

	@AppLogOmit
	@ExceptionHandler(RuntimeException.class)
	public Object handleSystemException(HttpServletRequest request, RuntimeException e) {
		log.error(ExceptionUtils.getStackTrace(e));
		
		ApiError apiError = new ApiError();
		apiError.setPath(request.getRequestURI());
		apiError.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setErrorMessage(TUtils.isProd(environment) ? "INTERNAL SERVER ERROR" : e.getMessage());
		return new ResponseEntity<ApiError>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
