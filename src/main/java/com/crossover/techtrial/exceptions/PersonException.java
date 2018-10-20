package com.crossover.techtrial.exceptions;

public class PersonException extends RuntimeException {

	private static final long serialVersionUID = 8068038995824487474L;

	private String value;
	private String resource;
	private String exceptionCause;

	public PersonException(String value, String resource, String exceptionCause) {
		super(exceptionCause);
		this.value = value;
		this.resource = resource;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getExceptionCause() {
		return exceptionCause;
	}

	public void setExceptionCause(String exceptionCause) {
		this.exceptionCause = exceptionCause;
	}

}
