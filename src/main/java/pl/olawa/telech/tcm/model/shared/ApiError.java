package pl.olawa.telech.tcm.model.shared;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

/*
 * Obiekt zwracany w przypadku wystąpienia błędu. Zobacz AbstractController.
 */
@Getter @Setter
public class ApiError {

	private HttpStatus statusCode;
	private String errorMessage;
	private LocalDateTime timestamp;
	private String path;
	

	public ApiError() {
		timestamp = LocalDateTime.now();
	}

}
