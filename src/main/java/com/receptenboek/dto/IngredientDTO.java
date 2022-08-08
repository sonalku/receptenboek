package com.receptenboek.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Ingredient DTO Class
 * @author Sonal Kumbhare
 * @version 1.0.0
 *
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Ingredient", value = "Ingredient Request")
public class IngredientDTO {

	@ApiModelProperty(dataType = "string", example = "Meat", position = 0, value = "The ingredient name")
	private String name;

	@ApiModelProperty(dataType = "string", example = "125 gr.", position = 1, value = "Amount for the ingredient")
	private String amount;
	
}
