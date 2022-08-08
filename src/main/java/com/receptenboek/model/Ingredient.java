package com.receptenboek.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Ingredient Class
 * @author Sonal Kumbhare
 * @version 1.0.0
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Ingredient", value = "Ingredient Response")
public class Ingredient {
	
	@ApiModelProperty(dataType = "string", example = "Meat", position = 0, value = "The ingredient name")
	private String name;
	@ApiModelProperty(dataType = "string", example = "125 gr.", position = 1, value = "Amount for the ingredient")
	private String amount;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
}
