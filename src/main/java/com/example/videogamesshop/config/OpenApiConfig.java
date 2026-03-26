package com.example.videogamesshop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Video Games Shop API")
                        .description("REST API for managing games, developers, publishers,"
                                + " categories and users")
                        .version("v1")
                        .contact(new Contact().name("Video Games Shop Team"))
                        .license(new License().name("Internal use")));
    }
}
