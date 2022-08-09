package com.receptenboek.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.receptenboek.controller.ReceptenboekController;
import com.receptenboek.dto.IngredientDTO;
import com.receptenboek.dto.RecipeDTO;
import com.receptenboek.enums.Category;
import com.receptenboek.model.Recipe;
import com.receptenboek.service.ReceptenboekService;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

/*@EnableConfigurationProperties
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@TestPropertySource(properties = { "spring.mongodb.embedded.version=4.0.21"})
@SpringBootTest*/
public class EmbeddedMongoTest{

    private static final String DATABASE_NAME = "embedded";

    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;
    private static MongoClient mongo;
    private static MongoTemplate mongoTemplate;
    
    @Autowired
	MockMvc mockMvc;

	private RecipeDTO recipe;

//	@Autowired
//	private ReceptenboekController controller;
    /*****************************************/
	@Mock
	private ReceptenboekService service;
	//private ReceptenboekRepository receptenboekRepository;
	@Autowired
	private ReceptenboekController controller;

	ObjectMapper objectMapper = new ObjectMapper();
	ObjectWriter objectWriter = objectMapper.writer();
    /**
     * @throws Exception 
     * @throws JsonProcessingException ***************************************/
    @BeforeEach
	public void setupBeforeEachTest() throws JsonProcessingException, Exception {
		
		System.out.println("Before each");
		//mongoTemplate.getDb().drop();
		
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		System.out.println("Called Before Test");
		List<String> instructions = new ArrayList();
		instructions.add("Boil");
		instructions.add("Fry");
		instructions.add("Cook");
		List<IngredientDTO> ingredients = new ArrayList();
		ingredients.add(new IngredientDTO("Potato", "3"));
		ingredients.add(new IngredientDTO("Tomato", "3"));

		recipe = new RecipeDTO();

		recipe.setTime(10.0);
		recipe.setTitle("Veg Maratha");
		recipe.setCategory(Category.VEG);
		recipe.setServings("6");
		recipe.setInstructions(instructions);
		recipe.setIngredients(ingredients);
		
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post("/recipes/").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(objectMapper.writeValueAsString(recipe)))
				.andExpect(status().isCreated())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
		assertThat(mvcResult.getResponse().getContentAsString().contains("Veg Maratha"));
	}
    @BeforeAll
    public static void beforeEach() throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        String bindIp = "localhost";
        int port = 12345;
        MongodConfig mongodConfig = MongodConfig.builder()
            .version(Version.Main.PRODUCTION)
            .net(new Net(bindIp, port, Network.localhostIsIPv6()))
            .build();
        mongodExe = starter.prepare(mongodConfig);
        mongod = mongodExe.start();
        mongo = MongoClients.create("mongodb://localhost:12345");
        mongoTemplate = new MongoTemplate(mongo, "receptenboek");
		TextIndexDefinition index = 
				new TextIndexDefinitionBuilder()
				.onField("instructions", 1f)
				.onField("servings", 1f).build();
		mongoTemplate.indexOps(Recipe.class).ensureIndex(index);

		//return mongoTemplate;
    }
    //@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongo, "receptenboek");
		TextIndexDefinition index = 
				new TextIndexDefinitionBuilder()
				.onField("instructions", 1f)
				.onField("servings", 1f).build();
		mongoTemplate.indexOps(Recipe.class).ensureIndex(index);

		return mongoTemplate;
	}
    @AfterAll
    public static void afterEach() throws Exception {
        if (mongod != null) {
            mongod.stop();
            mongodExe.stop();
        }
    }

   // @Test
    public void shouldCreateNewObjectInEmbeddedMongoDb() {
        // given
        MongoDatabase db = mongo.getDatabase(DATABASE_NAME);
        db.createCollection("testCollection");
        MongoCollection<BasicDBObject> col = db.getCollection("testCollection", BasicDBObject.class);

        // when
        col.insertOne(new BasicDBObject("testDoc", new Date()));

        // then
        assertEquals(1L, col.countDocuments());
    }
}