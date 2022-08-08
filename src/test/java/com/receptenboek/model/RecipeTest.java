package com.receptenboek.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

class RecipeTest {

	@Test
	void testEmptyConstructorAndToString() throws Exception {
		assertNotNull(new Recipe());
		assertFalse(new Recipe().toString().isBlank());
	}

	@Test
	void testFullConstrcutorAndGettersAndSetters() throws Exception {
		var recipe = new Recipe("ID", "TITLE", null, null, 1.1d, "USERID", null, null);

		assertEquals("ID", recipe.getId());
		assertEquals("TITLE", recipe.getTitle());
		assertEquals(1.1d, recipe.getTime());
		assertEquals("USERID", recipe.getUserId());
		assertNull(recipe.getIngredients());
		assertNull(recipe.getInstructions());

		recipe.setId("ID:1");
		recipe.setTitle("TITLE:1");
		recipe.setUserId("USERID:1");
		recipe.setTime(0d);
		recipe.setIngredients(List.of());
		recipe.setInstructions(List.of());

		assertEquals("ID:1", recipe.getId());
		assertEquals("TITLE:1", recipe.getTitle());
		assertEquals(0d, recipe.getTime());
		assertEquals("USERID:1", recipe.getUserId());
		assertNotNull(recipe.getIngredients());
		assertNotNull(recipe.getInstructions());

	}
}
