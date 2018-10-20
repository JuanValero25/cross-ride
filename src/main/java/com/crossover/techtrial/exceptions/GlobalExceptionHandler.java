package com.crossover.techtrial.exceptions;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crossover.techtrial.dto.ExceptionDTO;

import io.jsonwebtoken.JwtException;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ExceptionDTO> handle(Exception exception) {
		exception.printStackTrace();
		ExceptionDTO details = new ExceptionDTO("unexpected Exception", null, exception.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(details);
	}

	@ExceptionHandler({ ModelValidationException.class })
	public @ResponseBody ResponseEntity<List<ExceptionDTO>> handleModelValidationException(
			ModelValidationException exception) {
		List<ExceptionDTO> details = exception.getErrors().getAllErrors().stream()
				.map(f -> new ExceptionDTO(f.getObjectName(), f.getCode(), f.getDefaultMessage()))
				.collect(Collectors.toList());
		return ResponseEntity.status(exception.getCode()).body(details);
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public @ResponseBody ResponseEntity<ExceptionDTO> handleConstraintViolationException(
			ConstraintViolationException exception) {
		ExceptionDTO details = new ExceptionDTO(exception.getConstraintName(), exception.getSQLException().getMessage(),
				exception.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(details);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public @ResponseBody ResponseEntity<ExceptionDTO> handleMethodArgumentNotValidException(
			ConstraintViolationException exception) {
		ExceptionDTO details = new ExceptionDTO(exception.getConstraintName(), exception.getSQLException().getMessage(),
				exception.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(details);
	}

	@ExceptionHandler({ PersonException.class })
	public @ResponseBody ResponseEntity<ExceptionDTO> handlePersonException(PersonException exception) {
		ExceptionDTO details = new ExceptionDTO(exception.getResource(), exception.getValue(), exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(details);
	}

	@ExceptionHandler({ InternalAuthenticationServiceException.class })
	public @ResponseBody ResponseEntity<ExceptionDTO> handleInternalAuthenticationServiceException(
			InternalAuthenticationServiceException exception) {
		ExceptionDTO details = new ExceptionDTO("user not found or not problem with authentication", null,
				exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(details);
	}

	@ExceptionHandler({ DateTimeParseException.class })
	public @ResponseBody ResponseEntity<ExceptionDTO> handleDateTimeParseException(DateTimeParseException exception) {
		ExceptionDTO details = new ExceptionDTO("Bad formated or empty,  please check your date format is yyyy-MM-dd'T'HH:mm:ss", null,
				exception.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST ).body(details);
	}

	@ExceptionHandler({ JwtException.class })
	public @ResponseBody ResponseEntity<ExceptionDTO> handleJwtException(JwtException exception) {
		ExceptionDTO details = new ExceptionDTO("token", null, exception.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(details);
	}
}
