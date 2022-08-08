package com.receptenboek.dto;

import java.util.List;

import com.receptenboek.model.FilterIntruction;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Ingredient DTO Class
 * 
 * @author Sonal Kumbhare
 * @version 1.0.0
 *
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Filters", value = "Filters for Random serach")
public class FiltersDTO {

	private List<FilterIntruction> searchFilters;
}
