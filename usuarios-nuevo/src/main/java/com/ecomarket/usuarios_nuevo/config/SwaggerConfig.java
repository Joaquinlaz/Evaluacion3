package com.ecomarket.usuarios_nuevo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API de Usuarios - Ecomarket",
        version = "1.0",
        description = "Documentación de la API del microservicio de Usuarios para Ecomarket"
    )
)
public class SwaggerConfig {
    // No se necesita código aquí con springdoc-openapi-starter-webmvc-ui,
    // las anotaciones @OpenAPIDefinition y @Configuration son suficientes.
}