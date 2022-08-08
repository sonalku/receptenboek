package com.receptenboek.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.receptenboek.dto.IngredientDTO;
import com.receptenboek.dto.RecipeDTO;
import com.receptenboek.exception.ReceptenboekException;

class ValidationsTest {

	@InjectMocks
	private Validations validations;

	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testStringValidation() throws Exception {
		assertDoesNotThrow(() -> {
			validations.string("FIELD", "ID");
		});

		assertThrows(ReceptenboekException.class, () -> validations.string("FIELD", null));
		assertThrows(ReceptenboekException.class, () -> validations.string("FIELD", ""));
	}

	@Test
	void testIdValidation() throws Exception {
		assertDoesNotThrow(() -> {
			validations.id("ID");
		});

		assertThrows(ReceptenboekException.class, () -> validations.id(null));
		assertThrows(ReceptenboekException.class, () -> validations.id(""));
	}

	@Test
	void testOptionalIdValidation() throws Exception {
		assertDoesNotThrow(() -> {
			validations.optionalId(Optional.empty());
		});
		assertDoesNotThrow(() -> {
			validations.optionalId(Optional.of("ID"));
		});
	}

	@Test
	void testListStrings() throws Exception {

		assertDoesNotThrow(() -> {
			validations.listStrings("FIELD", List.of("X", "Y"));
		});

		assertDoesNotThrow(() -> {
			validations.listStrings("FIELD", List.of());
		});
	}

	@Test
	void tesRecipeValidation() throws Exception {
		assertDoesNotThrow(() -> {
			validations
					.recipe(new RecipeDTO("TITLE", List.of(new IngredientDTO("NAME", "AMOUNT")), List.of("STEP"), 10, null, null));
		});

		assertThrows(ReceptenboekException.class, () -> validations.recipe(null));
		assertThrows(ReceptenboekException.class, () -> validations.recipe(new RecipeDTO(null, null, null, -1d, null, null)));
		assertThrows(ReceptenboekException.class, () -> validations.recipe(new RecipeDTO("", null, null, 1d, null, null)));
		assertThrows(ReceptenboekException.class, () -> validations.recipe(new RecipeDTO("TITLE", null, List.of(),-1d,null,null)));
		assertThrows(ReceptenboekException.class, () -> validations.recipe(new RecipeDTO("TITLE", null, null, 1d, null, null)));
		assertThrows(ReceptenboekException.class,
				() -> validations.recipe(new RecipeDTO("TITLE", null, List.of("STEP"), 1d, null, null)));
		assertThrows(ReceptenboekException.class,
				() -> validations.recipe(new RecipeDTO("TITLE", null, List.of("STEP"), 1d, null, null)));

	}

	@Test
	void testValueValidation() throws Exception {
		assertDoesNotThrow(() -> validations.value("VALUE"));
		assertThrows(ReceptenboekException.class, () -> validations.value(""));
		assertThrows(ReceptenboekException.class, () -> validations.value(null));

	}
}
