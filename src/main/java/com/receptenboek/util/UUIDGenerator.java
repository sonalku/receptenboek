package com.receptenboek.util;

import java.util.UUID;

/**
 * 
 * UUID generator interface
 */
public interface UUIDGenerator {

	/**
	 * Default generate UUID
	 *
	 * @return the string
	 */
	default String generateUUID() {
		return UUID.randomUUID().toString();
	}
}
