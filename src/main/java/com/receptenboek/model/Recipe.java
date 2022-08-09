package com.receptenboek.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.receptenboek.enums.Category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Recipe Class
 * @author Sonal Kumbhare
 * @version 1.0.0	
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Recipe for response , include de ID", value = "Recipe Response")
@Document(collection = "recipes")
@CompoundIndexes({
    @CompoundIndex(name = "title_serving",unique = true, def = "{'title' : 1, 'servings': 1}", background = true)
})
public class Recipe {

	@ApiModelProperty(dataType = "string", example = "123e4567-e89b-42d3-a456-556642440000", position = 0, value = "The ID for Product, it's a UUID")
	@Id
	private String id;

	@ApiModelProperty(dataType = "string", example = "Fries Eggs", position = 1, value = "The title for the recipe")
	@Indexed(unique=true, background = true)
	private String title;

	@ApiModelProperty(position = 2, value = "The List of ingredients")
	private List<Ingredient> ingredients;

	@ApiModelProperty(position = 3, value = "The List of steps")
	private List<String> instructions;

	@ApiModelProperty(dataType = "double", example = "1.23", position = 4, value = "The time for recipe in hours")
	private double time;

	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private String userId;
	
	@ApiModelProperty(dataType = "string", example = "VEG/NONVEG", value = "The Category for the recipe", required = true)
	private Category category;
	
	@ApiModelProperty(dataType = "String", example = "2", value = "Number of person the recipe sufficient for", required = true)
	private String servings;
}
