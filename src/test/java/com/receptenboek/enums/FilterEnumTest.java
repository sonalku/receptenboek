package com.receptenboek.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FilterEnumTest {

	@Test
	void testGetterAndFromName() throws Exception {
		var time = "time";
		var title = "title";
		var other = "other";

		assertTrue(FilterEnum.fromName(time).isPresent());
		assertTrue(FilterEnum.fromName(title).isPresent());
		assertTrue(FilterEnum.fromName(other).isEmpty());

		assertEquals(title, FilterEnum.TITLE.getName());

	}

	@Test
	void testToBeSended() throws Exception {
		assertNotNull(FilterEnum.toBeSended());
		assertNotNull(FilterEnum.toBeSended());
	}
}
