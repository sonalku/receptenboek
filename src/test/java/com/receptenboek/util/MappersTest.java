package com.receptenboek.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.receptenboek.dto.IngredientDTO;
import com.receptenboek.dto.RecipeDTO;
import com.receptenboek.model.Ingredient;

class MappersTest {

	@Test
	void testDtos() throws Exception {
		var mappers = new Mappers() {
		};
		var ingredient = mappers.dtoToIngredient(new IngredientDTO("NAME", "AMOUNT"));
		assertEquals("AMOUNT", ingredient.getAmount());
		assertEquals("NAME", ingredient.getName());

		var recipe = mappers.dtoToRecipe(new RecipeDTO("TITLE", new ArrayList(), new ArrayList(), 0d, null, null));
		assertEquals("TITLE", recipe.getTitle());
		assertNotNull(recipe.getIngredients());
		assertNotNull(recipe.getInstructions());
		assertEquals(0d, recipe.getTime());
	}

	@Test
	void testIngredientToDocument() throws Exception {
		var mapper = new Mappers() {
		};

		assertNotNull(mapper.ingredientToDocument(new Ingredient("NAME", "INGREDIENT")));
	}
}
