
package com.ecomarket.ecomarket.Swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Swagger {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Pedidos - Ecomarket")
                        .version("1.0.0")
                        .description("Documentación de la API para la gestión de pedidos")
                        .contact(new Contact()
                                .name("Soporte Ecomarket")
                                .email("soporte@ecomarket.com")
                        )
                );
    }
}
