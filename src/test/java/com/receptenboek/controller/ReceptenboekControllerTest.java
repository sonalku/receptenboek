package com.receptenboek.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.receptenboek.dto.FiltersDTO;
import com.receptenboek.dto.IngredientDTO;
import com.receptenboek.dto.RecipeDTO;
import com.receptenboek.enums.Category;
import com.receptenboek.enums.FilterCriteria;
import com.receptenboek.enums.FilterEnum;
import com.receptenboek.model.FilterIntruction;
import com.receptenboek.model.Recipe;
import com.receptenboek.repository.ReceptenboekRepository;
import com.receptenboek.service.ReceptenboekService;
import com.receptenboek.util.Mappers;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

@EnableConfigurationProperties
@AutoConfigureMockMvc
@AutoConfigureDataMongo
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class, MongoDataAutoConfiguration.class })
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ReceptenboekControllerTest implements Mappers {

	@Mock
	private ReceptenboekService service;

	@Autowired
	MockMvc mockMvc;

	private RecipeDTO recipe;

	@Autowired
	private ReceptenboekRepository receptenboekRepository;
	@Autowired
	private ReceptenboekController controller;

	private ObjectMapper objectMapper = new ObjectMapper();

	/*************************************************/
	private static MongodExecutable mongodExe;
	private static MongodProcess mongod;
	private static MongoClient mongo;
	private static MongoTemplate mongoTemplate;
	/*************************************************/
	
	@BeforeAll
	public static void beforeEach() throws Exception {
		MongodStarter starter = MongodStarter.getDefaultInstance();
		String bindIp = "localhost";
		int port = 12345;
		MongodConfig mongodConfig = MongodConfig.builder().version(Version.Main.PRODUCTION)
				.net(new Net(bindIp, port, Network.localhostIsIPv6())).build();
		mongodExe = starter.prepare(mongodConfig);
		mongod = mongodExe.start();
		mongo = MongoClients.create("mongodb://localhost:12345");
		mongoTemplate = new MongoTemplate(mongo, "receptenboek");
		TextIndexDefinition index = new TextIndexDefinitionBuilder().onField("instructions", 1f).onField("servings", 1f)
				.build();
		mongoTemplate.indexOps(Recipe.class).ensureIndex(index);

	}

	@AfterAll
	public static void afterEach() throws Exception {
		if (mongod != null) {
			mongod.stop();
			mongodExe.stop();	
		}
		mongoTemplate.getDb().drop();
	}

	@BeforeEach
	public void setupBeforeEachTest() {

		System.out.println("Before each");

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
	}

	@Test
	void testIdOk() throws Exception {
		when(service.findById(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(new Recipe()));
		Recipe recipeDoc = dtoToRecipe(recipe);
		recipeDoc.setId("123e4567-e89b-42d3-a456-556642440000");

		Recipe recipeOut = receptenboekRepository.save(recipeDoc);
		System.out.println(recipeOut);
		mockMvc.perform(MockMvcRequestBuilders.get("/recipes/{id}", "123e4567-e89b-42d3-a456-556642440000")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("id").exists());
	}

	@Test
	void testCreateRecipeCreated() throws Exception {

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post("/recipes/").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(recipe)))
				.andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print()).andReturn();
		assertThat(mvcResult.getResponse().getContentAsString().contains("Veg Maratha"));
	}

	@Test
	void testByIdNoContent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/recipes/{id}", "abc")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isNotFound());
	}

	@Test
	void testIngredientsOk() throws Exception {
		when(service.findById(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(new Recipe()));
		Recipe recipeDoc = dtoToRecipe(recipe);
		recipeDoc.setId("123e4567-e89b-42d3-a456-556642440000");

		List<String> names = new ArrayList();
		names.add("Potato");
		names.add("Tomato");

		Recipe recipeOut = receptenboekRepository.save(recipeDoc);
		System.out.println(recipeOut);
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/recipes/search/ingredients/")
						.param("names", String.valueOf(names.get(0)), String.valueOf(names.get(1)))
						.accept(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()).andReturn();

		assertThat(result.getResponse().getContentAsString().contains("Potato"));
	}

	@Test
	void testByFiltersCriteria() throws Exception {

		FiltersDTO dto = new FiltersDTO();
		FilterIntruction instr = new FilterIntruction();
		instr.setCriteria(FilterCriteria.INCLUDE);
		instr.setFilter(FilterEnum.TITLE);
		instr.setValue("Maratha");
		List<FilterIntruction> list = new ArrayList();
		list.add(instr);
		dto.setSearchFilters(list);

		Recipe recipeDoc = dtoToRecipe(recipe);
		recipeDoc.setId("123e4567-e89b-42d3-a456-556642440000");

		receptenboekRepository.save(recipeDoc);

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post("/recipes/search/")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(dto)))
				.andDo(MockMvcResultHandlers.print()).andExpect(status().isAccepted())
				.andExpect(jsonPath("$[0].title", is("Veg Maratha"))).andReturn();

		assertThat(result.getResponse().getContentAsString().contains("Veg Maratha"));
	}
    
}
