package com.receptenboek.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Component;

import com.receptenboek.enums.FilterCriteria;
import com.receptenboek.enums.FilterEnum;
import com.receptenboek.model.FilterIntruction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Utility {

	/**
	 * Fetch Text Criteria List : In USE
	 * 
	 * @param filterIntructions
	 * @return
	 */
	public List<TextCriteria> getTextCriteriaList(List<FilterIntruction> filterIntructions) {
		
		List<TextCriteria> textcriteriaList = new ArrayList<TextCriteria>();

		for (FilterIntruction instr : filterIntructions) {

			if (isTextCriteria(instr)) {
				String criteria = instr.getCriteria().getName();
				String filter = instr.getFilter().getName();
				String value = instr.getValue();
				log.info("Text Search on Ingrediants only");
				log.info("CRITERIA = " + criteria);
				log.info("FILTER = " + filter);
				log.info("VALUE =" + value);
				if (criteria.equalsIgnoreCase(FilterCriteria.INCLUDE.name())) {
					if (!filter.equalsIgnoreCase(FilterEnum.SERVINGS.getName()))
						textcriteriaList.add(TextCriteria.forDefaultLanguage().matchingAny(filter, value));
					else
						textcriteriaList.add(TextCriteria.forDefaultLanguage().matchingAny(filter, String.valueOf(value)));
				} else if (criteria.equalsIgnoreCase(FilterCriteria.EXCLUDE.name())) {
					if (!filter.equalsIgnoreCase(FilterEnum.SERVINGS.getName()))
						textcriteriaList.add(TextCriteria.forDefaultLanguage().notMatchingAny(filter, value));
					else
						textcriteriaList.add(TextCriteria.forDefaultLanguage().notMatchingAny(filter, String.valueOf(value)));
				}
			}
		}
		return textcriteriaList;
	}

	/**
	 * Apply Search Criteria and return Query : USE
	 * 
	 * @param filterIntructions
	 * @param newQuery
	 * @return
	 */
	public List<Criteria> getCriteriaList(List<FilterIntruction> filterIntructions) {

		List<Criteria> criteriaList = new ArrayList<Criteria>();
		
		for (FilterIntruction instr : filterIntructions) {
			String criteria = instr.getCriteria().getName();
			String filter = instr.getFilter().getName();
			String value = instr.getValue();
			if (!isTextCriteria(instr)) {
				log.info("Non Igredients");
				log.info("CRITERIA = " + instr.getCriteria().getName());
				log.info("FILTER = " + instr.getFilter().getName());
				log.info("VALUE =" + instr.getValue());

				if (criteria.equalsIgnoreCase(FilterCriteria.INCLUDE.name())) {
					if (!isFixedCriteria(instr))
						criteriaList.add(
								Criteria.where(filter).regex(".*" + instr.getValue() + ".*", "i"));
					else if (filter.equalsIgnoreCase(FilterEnum.INGREDIENTS.getName()))
						criteriaList.add(Criteria.where(filter).elemMatch(Criteria.where("name").is(value)));
					else
						criteriaList.add(Criteria.where(filter).is(instr.getValue()));
				} else if (criteria.equalsIgnoreCase(FilterCriteria.EXCLUDE.name())) {
					if (!isFixedCriteria(instr))
						criteriaList.add(Criteria.where(filter)
								.regex(".*" + instr.getValue() + ".*", "i").not());
					else if (filter.equalsIgnoreCase(FilterEnum.INGREDIENTS.getName()))
						criteriaList.add(Criteria.where(filter).elemMatch(Criteria.where("name").ne(value)));
					else
						criteriaList.add(Criteria.where(filter).is(instr.getValue()).not());
				}
			}
		}
		return criteriaList;
	}

	/**
	 * Fixed Criteria
	 * @param instr
	 * @return
	 */
	private boolean isFixedCriteria(FilterIntruction instr) {

		if (instr.getFilter().name().equalsIgnoreCase(FilterEnum.CATEGORY.name())
				|| instr.getFilter().name().equalsIgnoreCase(FilterEnum.INGREDIENTS.name())) {
			return true;
		} else
			return false;
	}

	/**
	 * Text Criteria
	 * @param instr
	 * @return
	 */
	private boolean isTextCriteria(FilterIntruction instr) {

		if (instr.getFilter().getName().equalsIgnoreCase(FilterEnum.INSTRUCTIONS.name())
				|| instr.getFilter().getName().equalsIgnoreCase(FilterEnum.SERVINGS.name())) {
			return true;
		} else
			return false;
	}
}
