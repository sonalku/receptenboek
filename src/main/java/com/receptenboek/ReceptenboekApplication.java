package com.receptenboek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;

/**
 * Application Class
 * @author Sonal Kumbhare
 * @version 1.0.0
 *
 */
@SpringBootApplication(exclude=EmbeddedMongoAutoConfiguration.class)
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class ReceptenboekApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReceptenboekApplication.class, args);
	}

}
