package com.receptenboek.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBList;
import com.receptenboek.dto.FiltersDTO;
import com.receptenboek.dto.RecipeDTO;
import com.receptenboek.exception.ReceptenboekException;
import com.receptenboek.model.FilterIntruction;
import com.receptenboek.model.Recipe;
import com.receptenboek.repository.ReceptenboekRepository;
import com.receptenboek.service.ReceptenboekService;
import com.receptenboek.util.Mappers;
import com.receptenboek.util.UUIDGenerator;
import com.receptenboek.util.Utility;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class Recipe Service Implementation.
 * 
 * @author Sonal Kumbhare
 * @version 1.0.0
 * @see ReceptenboekService
 * @see Mappers
 * @see UUIDGenerator
 * 
 */
@Service
@Slf4j
public class ReceptenboekServiceImpl implements ReceptenboekService, Mappers, UUIDGenerator {

	@Autowired
	private ReceptenboekRepository cookbookRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private Utility utility;

	/** {@inherithDoc} */
	@Override
	public Optional<List<Recipe>> findAll(String userId) throws ReceptenboekException {
		log.info("--> RecipeServiceImpl findAll userId: {}", userId);
		return Optional.ofNullable(cookbookRepository.all(userId));
	}

	/** {@inherithDoc} */
	@Override
	public Optional<Recipe> findById(String userId, String id) throws ReceptenboekException {
		log.info("--> RecipeServiceImpl  findById userId: {}, id: {}", userId, id);
		return cookbookRepository.byId(userId, id);
	}

	/** {@inherithDoc} */
	@Override
	public Optional<List<Recipe>> findByIngredients(String userId, List<String> ingredientsNames)
			throws ReceptenboekException {
		log.info("--> RecipeServiceImpl  findByIngredients userId: {}, ingredients: {}", userId, ingredientsNames);
		return Optional.ofNullable(cookbookRepository.byIngredients(userId, ingredientsNames.stream()
				.map(name -> Pattern.compile(name, Pattern.CASE_INSENSITIVE)).collect(Collectors.toList())));
	}

	/** {@inherithDoc} */
	@Override
	public Optional<Recipe> createRecipe(String userId, RecipeDTO recipe) throws ReceptenboekException {
		log.info("--> RecipeServiceImpl createRecipe userId: {}, recipe: {}", userId, recipe);

		var recipeToInsert = this.dtoToRecipe(recipe);

		recipeToInsert.setId(this.generateUUID());
		recipeToInsert.setUserId(userId);

		log.debug("--> RecipeServiceImpl createRecipe generated UUID: {}", recipeToInsert.getId());

		return Optional.ofNullable(cookbookRepository.save(recipeToInsert));
	}

	/** {@inherithDoc} */
	@Override
	public Optional<Recipe> updateRecipe(String userId, String id, RecipeDTO recipe) throws ReceptenboekException {
		log.info("--> RecipeServiceImpl updateRecipe userId: {}, id: {}, recipe: {}", userId, id, recipe);

		var oRecipeSaved = cookbookRepository.byId(userId, id);

		if (oRecipeSaved.isEmpty()) {
			throw new ReceptenboekException(HttpStatus.NOT_FOUND, "The id is not valid");
		}

		var recipeToSave = this.dtoToRecipe(recipe);
		var oldRecipe = oRecipeSaved.get();

		recipeToSave.setId(oldRecipe.getId());
		recipeToSave.setUserId(oldRecipe.getUserId());

		return Optional.ofNullable(cookbookRepository.save(recipeToSave));
	}

	/** {@inherithDoc} */
	@Override
	public void deleteRecipe(String userId, String id) throws ReceptenboekException {
		log.info("--> RecipeServiceImpl deleteRecipe: {} userId: {}, id: {}", userId, id);
		var oRecipeSaved = cookbookRepository.byId(userId, id);

		if (oRecipeSaved.isEmpty()) {
			throw new ReceptenboekException(HttpStatus.NOT_FOUND, "The id is not valid");
		}

		cookbookRepository.deleteById(oRecipeSaved.get().getId());

	}

	private String recipeToJSON(String userId, String id) throws ReceptenboekException {
		Optional<Recipe> recipe = findById(userId, id);
		if (recipe.isEmpty()) {
			throw new ReceptenboekException(HttpStatus.NOT_FOUND, "the id is not valid");
		}
		try {
			return new ObjectMapper().writeValueAsString(recipe.get());
		} catch (Exception e) {
			throw new ReceptenboekException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not convert recipe to JSON");
		}
	}

	private String recipesToJSON(String userId) throws ReceptenboekException {
		Optional<List<Recipe>> recipes = findAll(userId);
		if (recipes.isEmpty() || recipes.get().isEmpty()) {
			throw new ReceptenboekException(HttpStatus.NOT_FOUND, "the user haven't got any recipes");
		}
		try {
			return new ObjectMapper().writeValueAsString(recipes.get());
		} catch (Exception e) {
			throw new ReceptenboekException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not convert recipe to JSON");
		}

	}

	public ResponseEntity<List<Recipe>> findByFilterIntructions(FiltersDTO filtersDTO)
			throws ReceptenboekException {
		log.info(" --> RecipeServiceImpl  findbyFilter userId: {}, filter: {}, value: {}", filtersDTO);
		
		List<FilterIntruction> filterIntruction = filtersDTO.getSearchFilters();
	    Query newQuery = new Query();
	    
	    BasicDBList bsonList = new BasicDBList();
	    
	    for(TextCriteria textCr : utility.getTextCriteriaList(filterIntruction)) {
	    	 bsonList.add(textCr.getCriteriaObject());
	    }
	    for(Criteria criteria : utility.getCriteriaList(filterIntruction)) {
	    	bsonList.add(criteria.getCriteriaObject());
	    }
	    newQuery.addCriteria(new Criteria("$and").is(bsonList));
		
		List<Recipe> list = mongoTemplate.find(newQuery, Recipe.class);	
		
		return new ResponseEntity<List<Recipe>>(list,HttpStatus.ACCEPTED);
	}

}
