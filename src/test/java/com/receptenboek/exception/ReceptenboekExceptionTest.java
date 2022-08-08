package com.receptenboek.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ReceptenboekExceptionTest {

	@Test
	void testConstructors() throws Exception {
		var cbe = new ReceptenboekException(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.getReasonPhrase());
		assertNotNull(cbe);
		assertFalse(cbe.toString().isBlank());
		cbe = new ReceptenboekException(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.getReasonPhrase(), new Date().getTime());
		assertNotNull(cbe);
		assertFalse(cbe.toString().isBlank());
	}

	@Test
	void testToResponse() throws Exception {
		var response = new ReceptenboekException(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.getReasonPhrase()).toResponse();
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
		assertTrue(response.getBody().contains(HttpStatus.ACCEPTED.getReasonPhrase()));
	}

	@Test
	void testGettersJSONServiceError() throws Exception {
		var json =  new ReceptenboekException(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.getReasonPhrase()).getError();
		assertEquals(HttpStatus.ACCEPTED.getReasonPhrase(), json.getMessage());
		assertEquals(HttpStatus.ACCEPTED, json.getStatus());
		assertNotEquals(0L, json.getTimestamp());

	}
}
