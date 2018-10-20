package com.crossover.techtrial.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class ExceptionDTO. exception DTO for generic exception message
 */
public class ExceptionDTO {

	private final String resource;

	private final Object value;

	private final String message;

	/**
	 * Instantiates a new exception DTO.
	 *
	 * @param resource the resource
	 * @param value    the value
	 * @param message  the message
	 */
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public ExceptionDTO(@JsonProperty("resource") String resource, @JsonProperty("value") Object value,
			@JsonProperty("message") String message) {
		this.resource = resource;
		this.value = value;
		this.message = message;
	}

	/**
	 * Gets the resource.
	 *
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}
