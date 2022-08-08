package com.receptenboek.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UUIDGeneratorTest {

	@Test
	void testNoBlanck() throws Exception {
		assertFalse(new UUIDGenerator() {
		}.generateUUID().isBlank());
	}
}
