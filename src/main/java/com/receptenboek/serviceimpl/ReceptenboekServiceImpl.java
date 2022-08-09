package com.receptenboek.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	private ReceptenboekRepository receptenboekRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private Utility utility;

	/** {@inherithDoc} */
	@Override
	public Optional<List<Recipe>> findAll(String userId) throws ReceptenboekException {
		log.info("--> RecipeServiceImpl findAll userId: {}", userId);
		return Optional.ofNullable(receptenboekRepository.all(userId));
	}


	@Override
	public Optional<Map<String, Object>> findAll(String pageNumber, String pageSize) {
		List<RecipeDTO> tutorials = new ArrayList<RecipeDTO>();
	    Pageable paging = PageRequest.of(Integer.valueOf(pageNumber), Integer.valueOf(pageSize));

	    Page<Recipe> pages = receptenboekRepository.findAll(paging);
	    Map<String, Object> response = new HashMap<>();
	      response.put("tutorials", pages.getContent());
	      response.put("currentPage", pages.getNumber());
	      response.put("totalItems", pages.getTotalElements());
	      response.put("totalPages", pages.getTotalPages());
	      response.put("lastPage", pages.isLast());
	      return Optional.ofNullable(response);
	}
	
	/** {@inherithDoc} */
	@Override
	public Optional<Recipe> findById(String userId, String id) throws ReceptenboekException {
		log.info("--> RecipeServiceImpl  findById userId: {}, id: {}", userId, id);
		return receptenboekRepository.byId(userId, id);
	}

	/** {@inherithDoc} */
	@Override
	public Optional<List<Recipe>> findByIngredients(String userId, List<String> ingredientsNames)
			throws ReceptenboekException {
		log.info("--> RecipeServiceImpl  findByIngredients userId: {}, ingredients: {}", userId, ingredientsNames);
		return Optional.ofNullable(receptenboekRepository.byIngredients(userId, ingredientsNames.stream()
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

		return Optional.ofNullable(receptenboekRepository.save(recipeToInsert));
	}

	/** {@inherithDoc} */
	@Override
	public Optional<Recipe> updateRecipe(String userId, String id, RecipeDTO recipe) throws ReceptenboekException {
		log.info("--> RecipeServiceImpl updateRecipe userId: {}, id: {}, recipe: {}", userId, id, recipe);

		var oRecipeSaved = receptenboekRepository.byId(userId, id);

		if (oRecipeSaved.isEmpty()) {
			throw new ReceptenboekException(HttpStatus.NOT_FOUND, "The id is not valid");
		}

		var recipeToSave = this.dtoToRecipe(recipe);
		var oldRecipe = oRecipeSaved.get();

		recipeToSave.setId(oldRecipe.getId());
		recipeToSave.setUserId(oldRecipe.getUserId());

		return Optional.ofNullable(receptenboekRepository.save(recipeToSave));
	}

	/** {@inherithDoc} */
	@Override
	public void deleteRecipe(String userId, String id) throws ReceptenboekException {
		log.info("--> RecipeServiceImpl deleteRecipe: {} userId: {}, id: {}", userId, id);
		var oRecipeSaved = receptenboekRepository.byId(userId, id);

		if (oRecipeSaved.isEmpty()) {
			throw new ReceptenboekException(HttpStatus.NOT_FOUND, "The id is not valid");
		}

		receptenboekRepository.deleteById(oRecipeSaved.get().getId());

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
