package com.receptenboek.dto;

import java.util.List;

import com.receptenboek.enums.Category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The class RecipeDTO
 * 
 * @author Sonal Kumbhare
 * @version 1.0.0
 *
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Recipe for Request", value = "Recipe Request")
public class RecipeDTO {

	@ApiModelProperty(dataType = "string", example = "Fries Eggs", position = 0, value = "The title for the recipe")
	private String title;

	@ApiModelProperty(position = 1, value = "The List of ingredients")
	private List<IngredientDTO> ingredients;

	@ApiModelProperty(position = 2, value = "The List of steps")
	private List<String> instructions;

	@ApiModelProperty(dataType = "double", example = "1.23", position = 3, value = "The time for recipe in hours")
	private double time;
	
	@ApiModelProperty(dataType = "string", example = "VEG/NONVEG", value = "The Category for the recipe")
	private Category category;
	
	@ApiModelProperty(dataType = "String", example = "2", value = "Number of person the recipe sufficient for")
	private String servings;

}
