package com.receptenboek.enums;

import java.util.Optional;
import java.util.stream.Stream;

import lombok.Getter;

/**
 * Filter Category
 * 
 * @author Sonal KUmbhare
 * @version 1.0.0
 * 
 */
public enum Category {

	VEG("VEG"), NONVEG("NONVEG");

	@Getter
	private String name;

	Category(final String name) {
		this.name = name;
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
	public static Optional<Category> fromName(String name) {
		return Stream.of(Category.values()).filter(e -> name.equalsIgnoreCase(e.getName())).findAny();
	}
}