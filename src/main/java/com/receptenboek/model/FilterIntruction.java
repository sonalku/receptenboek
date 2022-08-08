package com.receptenboek.model;

import com.receptenboek.enums.FilterCriteria;
import com.receptenboek.enums.FilterEnum;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Ingredient Class
 * @author Sonal Kumbhare
 * @version 1.0.0
 *
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Filter Intstuction", value = "Filters Intruction")
public class FilterIntruction {

	private FilterEnum filter;
	private String value;
	private FilterCriteria criteria;
}
