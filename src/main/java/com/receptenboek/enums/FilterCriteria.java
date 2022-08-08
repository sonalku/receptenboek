package com.receptenboek.enums;

import java.util.Optional;
import java.util.stream.Stream;

import lombok.Getter;

/**
 * Filter Criteria
 * 
 * @author Sonal KUmbhare
 * @version 1.0.0
 * 
 */
public enum FilterCriteria {

	INCLUDE("INCLUDE"), EXCLUDE("EXCLUDE");

	@Getter
	private String name;

	FilterCriteria(final String criteria) {
       this.name = criteria;
   }

	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Return a filled {@link Optional} if the name matches with any of the enum.
	 * 
	 * @param name {@link String}
	 * @return {@link Optional}&lt;{@link FilterEnum}&gt;
	 */
	public static Optional<FilterCriteria> fromName(String name) {
		return Stream.of(FilterCriteria.values()).filter(e -> name.equalsIgnoreCase(e.getName())).findAny();
	}
}