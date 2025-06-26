package com.ecomarket.reportes.Swagger;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//http://localhost:8080/swagger-ui/index.html#/

@Configuration
public class Swagger {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Reportes - Ecomarket")
                        .version("1.0")
                        .description("Documentación de la API de Reportes")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Soporte Ecomarket")
                                .email("soporte@ecomarket.com")
                        )
                );
    }
}

