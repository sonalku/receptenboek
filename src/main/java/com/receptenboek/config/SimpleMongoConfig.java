package com.receptenboek.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.receptenboek.model.Recipe;
/**
 * Mongo Config
 * @author Sonal Kumbhare
 * @version 1.0.0
 *
 */
@Configuration
public class SimpleMongoConfig {

	@Bean
	public MongoClient mongo() {
		ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/receptenboek");
		MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
				.build();

		return MongoClients.create(mongoClientSettings);
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongo(), "receptenboek");
		TextIndexDefinition index = 
				new TextIndexDefinitionBuilder()
				.onField("instructions", 1f)
				.onField("servings", 1f).build();
		mongoTemplate.indexOps(Recipe.class).ensureIndex(index);

		return mongoTemplate;
	}
}