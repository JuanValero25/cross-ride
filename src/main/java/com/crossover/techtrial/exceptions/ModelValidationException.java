package com.crossover.techtrial.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

public class ModelValidationException extends RuntimeException {

	private static final long serialVersionUID = -4154808747502535931L;
	private final int code = HttpStatus.BAD_REQUEST.value();
	private Errors errors;

	public ModelValidationException(String message, Errors errors) {
		super(message);
		this.errors = errors;
	}

	public int getCode() {
		return code;
	}

	public Errors getErrors() {
		return errors;
	}
}
