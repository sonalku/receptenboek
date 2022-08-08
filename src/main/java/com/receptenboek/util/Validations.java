package com.receptenboek.util;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.receptenboek.dto.FiltersDTO;
import com.receptenboek.dto.IngredientDTO;
import com.receptenboek.dto.RecipeDTO;
import com.receptenboek.enums.Category;
import com.receptenboek.enums.FilterCriteria;
import com.receptenboek.enums.FilterEnum;
import com.receptenboek.exception.ReceptenboekException;
import com.receptenboek.model.FilterIntruction;

/**
 * Validations Class
 * 
 * @author Sonal Kumbhare
 * @version 1.0.0
 * @see ReceptenboekException
 */
@Component
public class Validations {

	/**
	 * Id.
	 *
	 * @param id the id
	 * @return the validations
	 * @throws ReceptenboekException the error service
	 */
	public void id(String id) throws ReceptenboekException {
		if (id == null) {
			throw mandatoryError("id");
		}

		if (id.isBlank()) {
			throw emptyError("id");
		}

	}

	/**
	 * Id.
	 *
	 * @param id the id
	 * @return the validations
	 * @throws ReceptenboekException the Recipebook exception
	 */
	public void optionalId(Optional<String> id) throws ReceptenboekException {
		if (id.isPresent()) {
			id(id.get());
		}
	}

	/**
	 * List strings.
	 *
	 * @param field   the field
	 * @param strings the strings
	 * @return the validations
	 * @throws ReceptenboekException the Recipebook exception
	 */
	public void listStrings(String field, List<String> strings) throws ReceptenboekException {
		for (String s : strings) {
			string(field, s);
		}
	}

	/**
	 * Product.
	 *
	 * @param recipe the recipe
	 * @return the validations
	 * @throws ReceptenboekException the error service
	 */
	public void recipe(RecipeDTO recipe) throws ReceptenboekException {
		if (recipe == null) {
			throw mandatoryError("recipe");
		}
		time(recipe.getTime());
		string("title", recipe.getTitle());
		if (recipe.getInstructions() == null || recipe.getInstructions().isEmpty()) {
			throw new ReceptenboekException(HttpStatus.BAD_REQUEST, "The steps is not valid");
		}
		for (String step : recipe.getInstructions()) {
			string("steps", step);
		}
		if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
			throw new ReceptenboekException(HttpStatus.BAD_REQUEST, "The steps is not valid");
		}
		for (IngredientDTO ingredient : recipe.getIngredients()) {
			ingredient(ingredient);
		}
	}

	/**
	 * Product.
	 *
	 * @param recipe the recipe
	 * @return the validations
	 * @throws ReceptenboekException the error service
	 */
	public void filterInstruction(FiltersDTO filters) throws ReceptenboekException {
		
		for (FilterIntruction instruction : filters.getSearchFilters()) {
			filter(instruction);
		}
	}

	/**
	 * Ingredient.
	 *
	 * @param ingredient the ingredient
	 * @return the validations
	 * @throws ReceptenboekException the Recipebook exception
	 */
	public void ingredient(IngredientDTO ingredient) throws ReceptenboekException {
		string("ingredient name", ingredient.getName());
		string("ingredient amount", ingredient.getAmount());
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the validations
	 * @throws ReceptenboekException the error service
	 */
	public void value(String value) throws ReceptenboekException {
		if (value == null) {
			throw mandatoryError("value");
		}
		if (value.isBlank()) {
			throw emptyError("value");
		}
	}

	/**
	 * Amount.
	 *
	 * @param time the time
	 * @return the validations
	 * @throws ReceptenboekException the error service
	 */
	public void time(double time) throws ReceptenboekException {
		if (time <= 0) {
			throw negativeOrZeroAmountError("time");
		}
	}

	/**
	 * String.
	 *
	 * @param field the field
	 * @param value the value
	 * @return the validations
	 * @throws ReceptenboekException the error service
	 */
	public void string(String field, String value) throws ReceptenboekException {
		if (value == null) {
			throw mandatoryError(field);
		}

		if (value.isBlank()) {
			throw emptyError(field);
		}
	}

	/**
	 * String.
	 *
	 * @param field the field
	 * @param value the value
	 * @return the validations
	 * @throws ReceptenboekException the error service
	 */
	public void filter(FilterIntruction filterIntruction) throws ReceptenboekException {
		if (filterIntruction.getValue() == null) {
			throw mandatoryError(filterIntruction.getFilter().name());
		}

		if (filterIntruction.getValue().isBlank()) {
			throw emptyError(filterIntruction.getFilter().name());
		}

		if (FilterEnum.fromName(filterIntruction.getFilter().name()).isPresent()) {
			switch (filterIntruction.getFilter()) {

			case CATEGORY:
				if(!Category.fromName(filterIntruction.getValue()).isPresent())
					throw mandatoryError(filterIntruction.getValue());
				
				if(FilterCriteria.fromName(filterIntruction.getCriteria().getName()).isPresent())
					break;
				else
					throw mandatoryError(filterIntruction.getCriteria().getName());
			default:
				if(FilterCriteria.fromName(filterIntruction.getCriteria().getName()).isPresent())
					break;
				else
					throw mandatoryError(filterIntruction.getCriteria().getName());
			}
		} else {
			throw mandatoryError(filterIntruction.getCriteria().getName());
		}
	}

	/**
	 * Negative or zero amount error.
	 *
	 * @param field the field
	 * @return the error service
	 */
	private ReceptenboekException negativeOrZeroAmountError(String field) {
		return new ReceptenboekException(HttpStatus.BAD_REQUEST,
				String.format("the %s can't be zero or less tha 0", field));
	}

	/**
	 * Mandatory error.
	 *
	 * @param field the field
	 * @return the error service
	 */
	private ReceptenboekException mandatoryError(String field) {
		return new ReceptenboekException(HttpStatus.BAD_REQUEST, String.format("The %s is mandatory", field));
	}

	/**
	 * Mandatory error.
	 *
	 * @param field the field
	 * @return the error service
	 */
	private ReceptenboekException mandatoryValueError(String field) {
		return new ReceptenboekException(HttpStatus.BAD_REQUEST, String.format("The %s should be only ", field));
	}
	
	/**
	 * Empty error.
	 *
	 * @param field the field
	 * @return the error service
	 */
	private ReceptenboekException emptyError(String field) {
		return new ReceptenboekException(HttpStatus.BAD_REQUEST, String.format("The %s can't be empty", field));
	}
}
