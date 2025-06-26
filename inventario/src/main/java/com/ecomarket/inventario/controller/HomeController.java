package com.ecomarket.inventario.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Esto indica a Spring que esta clase manejará solicitudes web REST
public class HomeController {

    @GetMapping("/") // Esta anotación mapea las solicitudes GET a la URL raíz (/)
    public String home() {
        // Puedes devolver un String, un objeto JSON, HTML, etc.
        return "¡Bienvenido a la API de Inventario de Ecomarket! Para acceder a los recursos, intenta /productos, /proveedores, etc.";
    }

    // Opcional: podrías añadir otro endpoint de ejemplo
    @GetMapping("/info") // Mapea a http://localhost:8080/info
    public String info() {
        return "Versión de la API: 1.0.0. Desarrollado por [Tu Nombre/Equipo].";
    }
}