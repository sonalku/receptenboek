package com.receptenboek.repository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.receptenboek.model.Recipe;
/**
 * Repository.
 * @author Sonal Kumbhare
 * @version 1.0.0
 */
public interface ReceptenboekRepository extends MongoRepository<Recipe, String> {

	@Query("{ userId: ?0 }")
	public List<Recipe> all(String userid);

	@Query("{ userId: ?0, _id: ?1 }")
	public Optional<Recipe> byId(String userID, String id);

	@Query("{ userId: ?0, \"ingredients.name\": { $in : ?1}}")
	public List<Recipe> byIngredients(String userId, List<Pattern> patterns);

	@Query("{ userId: ?0, time: { $lte: ?1 } }")
	public List<Recipe> byTime(String userId, double time);

	@Query("{ userId: ?0, title: { $regex: ?1, $options: \"i\" } }")
	public List<Recipe> byTitle(String userId, String title);

}
