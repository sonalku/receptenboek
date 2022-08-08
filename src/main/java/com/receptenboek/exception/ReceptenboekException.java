package com.receptenboek.exception;

import java.io.Serializable;
import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * Cook book Exception
 *
 * @author Sonal Kumbhare
 * @version 1.0.0
 */
@ToString
public class ReceptenboekException extends Exception {

	/** The error. */
	@Getter
	private final JSONServiceError error;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new error service.
	 *
	 * @param status  the status
	 * @param message the message
	 */
	public ReceptenboekException(HttpStatus status, String message) {
		this.error = new JSONServiceError(status, message);
	}

	/**
	 * Instantiates a new error service.
	 *
	 * @param status    the status
	 * @param message   the message
	 * @param timestamp the timestamp
	 */
	public ReceptenboekException(HttpStatus status, String message, long timestamp) {
		this.error = new JSONServiceError(status, message, timestamp);
	}

	/**
	 * Create a {@link ResponseEntity} with error data
	 *
	 * @return {@link ResponseEntity}
	 */
	public final ResponseEntity<String> toResponse() {
		return ResponseEntity.status(error.getStatus())
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).body(error.toString());
	}

	/**
	 * Class for manage error in ErrorServiceClass
	 * 
	 * @author AntonioAlejandro01 - www.antonioalejandro.com
	 * @version 1.0.0
	 * @apiNote Save all data for the error and can be converted into JSON with
	 *          method {@code toString()}
	 */
	@ApiModel(value = "Cookbook Error", description = "Object that return when error ocurred")
	public class JSONServiceError implements Serializable {

		/** The Constant serialVersionUID. */
		@JsonIgnore
		@ApiModelProperty(hidden = true)
		private static final long serialVersionUID = 1L;

		/** The status. */
		@ApiModelProperty(dataType = "integer", example = "400", value = "The HTTP code error")
		@Getter
		private final HttpStatus status;

		/** The message. */
		@ApiModelProperty(dataType = "string", example = "Field x is not valid", value = "the message of error for give more information about what happened")
		@Getter
		private final String message;

		/** The timestamp. */
		@ApiModelProperty(dataType = "string", example = "1617545715", value = "the exact time when the error ocurred")
		@Getter
		private final long timestamp;

		/** The Constant JSON_TEMPLATE. */
		@ApiModelProperty(hidden = true)
		private static final String JSON_TEMPLATE = "{\"timestamp\":%d,\"status\":%d,\"message\":\"%s\"}";

		/**
		 * Instantiates a new JSON service error.
		 *
		 * @param status  the status
		 * @param message the message
		 */
		public JSONServiceError(HttpStatus status, String message) {
			this.message = message;
			this.status = status;
			this.timestamp = new Date().getTime();
		}

		/**
		 * Instantiates a new JSON service error.
		 *
		 * @param status    the status
		 * @param message   the message
		 * @param timestamp the timestamp
		 */
		public JSONServiceError(HttpStatus status, String message, long timestamp) {
			this.message = message;
			this.status = status;
			this.timestamp = timestamp;
		}

		/**
		 * Convert Object into String (JSON)
		 * 
		 * @return {@link String} with her format is JSON
		 */
		@Override
		public String toString() {

			return String.format(JSON_TEMPLATE, timestamp, status.value(), message);
		}

	}

}
