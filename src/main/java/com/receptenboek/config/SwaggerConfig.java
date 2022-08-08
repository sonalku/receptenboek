package com.receptenboek.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Class SwaggerConfig.
 * 
 * @author Sonal Kumbhare
 * @version 1.0.0
 * @apiNote Swagger config
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private static final String LICENSE = "CGI License";
	private static final String VERSION = "1.0.0";
	private static final String TITLE = "Receptenboek";
	private static final String DESCRIPTION = "Service for manage all about Recipes";
	private static final String NAME = "Sonal Kumbhare";
	private static final String URL = "www.receptenboek.com";
	private static final String EMAIL = "ksonalkumbhare@gmail.com";
	private static final String BASE_PACKAGE = "com.receptenboek.controller";

	/**
	 * Users api.
	 *
	 * @return {@link Docket}
	 */
	@Bean
	public Docket usersApi() {
		return new Docket(DocumentationType.OAS_30).apiInfo(usersApiInfo()).select().paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE)).build();
	}

	/**
	 * Users api info.
	 *
	 * @return {@link ApiInfo}
	 */
	private ApiInfo usersApiInfo() {
		return new ApiInfoBuilder().title(TITLE).version(VERSION).license(LICENSE).description(DESCRIPTION)
				.contact(contact()).build();
	}

	/**
	 * Create a contact for userApiInfo
	 * 
	 * @return {@link Contact}
	 */
	private Contact contact() {
		return new Contact(NAME, URL, EMAIL);
	}

}