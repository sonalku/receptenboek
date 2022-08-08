package com.receptenboek;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

/**
 * The Class SwaggerConfig.
 * 
 * @author Sonal Kumbhare
 * @version 1.0.0
 */
@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class ReceptenboekApplicationTests {

	@DisplayName("given object to save"
	        + " when save object using MongoDB template"
	        + " then object is saved")
	    @Test
	    public void test(@Autowired MongoTemplate mongoTemplate) {
	    
		List<String> instructions = new ArrayList();
		instructions.add("Boil");
		instructions.add("Fry");
		instructions.add("Cook");
		
	        DBObject objectToSave = BasicDBObjectBuilder.start()
	            .add("time", "10")
	            .add("title", "Veg Maratha")
	            .add("category", "NONVEG")
	            .add("servings", "4")
	            .add("instructions", instructions)
	            .get();

	        // when
	        mongoTemplate.save(objectToSave, "collection");

	        // then
	        assertThat(mongoTemplate.findAll(DBObject.class, "collection")).extracting("title")
	            .containsOnly("Veg Maratha");
	    }
}
