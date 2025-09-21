package com.aruidev.kanbeeapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI kanbeeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Kanbee API")
                        .description("API REST para tableros Kanban colaborativos (sin autenticación)")
                        .version("v1")
                        .contact(new Contact().name("Kanbee").url("https://github.com/aruidev"))
                )
                .servers(List.of(
                        new Server().url("/").description("Default")
                ));
    }
}

