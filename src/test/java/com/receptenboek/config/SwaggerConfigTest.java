package com.receptenboek.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class SwaggerConfigTest {

	@Test
	void testNotNull() throws Exception {
		var config = new SwaggerConfig();
		assertNotNull(config);
		assertNotNull(config.usersApi());
	}
}
