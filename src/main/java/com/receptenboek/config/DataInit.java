package com.receptenboek.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.receptenboek.enums.Category;
import com.receptenboek.model.Ingredient;
import com.receptenboek.model.Recipe;
import com.receptenboek.repository.ReceptenboekRepository;
import com.receptenboek.util.UUIDGenerator;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * Sample Data Populator
 * @author Sonal Kumbhare
 *
 */
@Slf4j
@Component
public class DataInit implements ApplicationListener<ApplicationReadyEvent>,UUIDGenerator{

    private final ReceptenboekRepository myRepository;

    public DataInit(ReceptenboekRepository myRepository) {
        this.myRepository = myRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
    	
    	List<String> instructions = new ArrayList();
		instructions.add("Boil");
		instructions.add("Fry");
		instructions.add("Cook");
		List<Ingredient> ingredients = new ArrayList();
		ingredients.add(new Ingredient("Potato", "3"));
		ingredients.add(new Ingredient("Tomato", "3"));

		Recipe recipe = new Recipe();

		recipe.setId(this.generateUUID());
		recipe.setTime(10.0);
		recipe.setTitle("Veg Curry");
		recipe.setCategory(Category.VEG);
		recipe.setServings("6");
		recipe.setInstructions(instructions);
		recipe.setIngredients(ingredients);
		
		List<String> instructions1 = new ArrayList();
		instructions1.add("Boil");
		instructions1.add("Fry");
		instructions1.add("Cook");
		List<Ingredient> ingredients1 = new ArrayList();
		ingredients1.add(new Ingredient("Chicken", "3"));
		ingredients1.add(new Ingredient("Egg", "3"));

		Recipe recipe1 = new Recipe();

		recipe1.setId(this.generateUUID());
		recipe1.setTime(10.0);
		recipe1.setTitle("Chicken Curry");
		recipe1.setCategory(Category.NONVEG);
		recipe1.setServings("4");
		recipe1.setInstructions(instructions1);
		recipe1.setIngredients(ingredients1);
		
		List<String> instructions2 = new ArrayList();
		instructions2.add("Boil");
		instructions2.add("Mix");
		instructions2.add("Fry");
		instructions2.add("Cook");
		List<Ingredient> ingredients2 = new ArrayList();
		ingredients2.add(new Ingredient("Boil Egg", "3"));
		ingredients2.add(new Ingredient("Egg", "3"));

		Recipe recipe2 = new Recipe();

		recipe2.setId(this.generateUUID());
		recipe2.setTime(10.0);
		recipe2.setTitle("Egg Curry");
		recipe2.setCategory(Category.NONVEG);
		recipe2.setServings("4");
		recipe2.setInstructions(instructions2);
		recipe2.setIngredients(ingredients2);
		
        //myRepository.save(recipe);
		log.info("Inserted : ==> "+myRepository.save(recipe));
		log.info("Inserted : ==> "+myRepository.save(recipe1));
		log.info("Inserted : ==> "+myRepository.save(recipe2));
    }
}