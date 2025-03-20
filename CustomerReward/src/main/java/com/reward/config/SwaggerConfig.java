package com.reward.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
@Configuration
public class SwaggerConfig {

    @Bean // Declares this method as a Spring Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()) // Initializes OpenAPI components
                .info(new Info().title("Sample Swagger API Documentation") // Sets the API title
                        .description("This document provides API details for a transaction reward point Spring Boot Project")); // Sets the API description
    }
}
