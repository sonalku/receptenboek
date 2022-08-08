package com.receptenboek.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.receptenboek.dto.FiltersDTO;
import com.receptenboek.dto.RecipeDTO;
import com.receptenboek.exception.ReceptenboekException;
import com.receptenboek.model.Recipe;

/**
 * Recipe Service Interface
 * @author Sonal Kumbhare
 * @version 1.0.0
 *
 */
public interface ReceptenboekService {

	/**
	 * Find All recipes associated a userId passed as parameter
	 * 
	 * @param userId {@link String}
	 * @return {@link Optional}&lt;{@link List}&lt;{@link Recipe}&gt;&gt;
	 * @throws ReceptenboekException
	 */
	public Optional<List<Recipe>> findAll(String userId) throws ReceptenboekException;

	/**
	 * Find a recipe by id and userId
	 * 
	 * @param userId {@link String}
	 * @param id     {@link String}
	 * @return {@link Optional}&lt;{@link Recipe}&gt;
	 * @throws ReceptenboekException
	 */
	public Optional<Recipe> findById(String userId, String id) throws ReceptenboekException;

	/**
	 * Find recipes that already one ingredients names matches with names list
	 * passed as parameter parameter
	 * 
	 * @param userId           {@link String}
	 * @param ingredientsNames {@link List}&lt;{@link String}&gt;
	 * @return {@link Optional}&lt;{@link List}&lt;{@link Recipe}&gt;&gt;
	 * @throws ReceptenboekException
	 */
	public Optional<List<Recipe>> findByIngredients(String userId, List<String> ingredientsNames)
			throws ReceptenboekException;

	/**
	 * find Recipes with a couple of filter and her value.
	 * 
	 * @param userId {@link String}
	 * @param filter {@link String}
	 * @param value  {@link String}
	 * @return {@link Optional}&lt;{@link List}&lt;{@link Recipe}&gt;&gt;
	 * @throws ReceptenboekException
	 */
	public ResponseEntity<List<Recipe>> findByFilterIntructions(FiltersDTO filterIntruction) throws ReceptenboekException;
	
	/**
	 * Save a recipe
	 * 
	 * @param userId {@link String}
	 * @param recipe {@link RecipeDTO}
	 * @return {@link Optional}&lt;{@link Recipe}&gt;
	 * @throws ReceptenboekException
	 */
	public Optional<Recipe> createRecipe(String userId, RecipeDTO recipe) throws ReceptenboekException;

	/**
	 * Update a recipe
	 * 
	 * @param userId {@link String}
	 * @param id     {@link String}
	 * @param recipe {@link RecipeDTO}
	 * @return {@link Optional}&lt;{@link Recipe}&gt;
	 * @throws ReceptenboekException
	 */
	public Optional<Recipe> updateRecipe(String userId, String id, RecipeDTO recipe) throws ReceptenboekException;

	/**
	 * Delete a recipe
	 * 
	 * @param userId {@link String}
	 * @param id     {@link String}
	 * @throws ReceptenboekException
	 */
	public void deleteRecipe(String userId, String id) throws ReceptenboekException;

}
