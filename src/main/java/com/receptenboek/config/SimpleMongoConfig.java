package com.receptenboek.config;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("${spring.data.mongodb.port}")
	private String port;
	
	@Value("${spring.data.mongodb.database}")
	private String database;
	
	@Bean
	public MongoClient mongo() {
		StringBuilder builder = new StringBuilder();
		builder.append("mongodb://localhost:").append(port).append("/").append(database);
		System.out.println("Connection : =>"+builder.toString());
		ConnectionString connectionString = new ConnectionString(builder.toString());
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