package com.ecomarket.usuarios_nuevo.model;

import jakarta.persistence.Entity; // Asegúrate de que sea jakarta.persistence
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Genera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Entity
@Table(name = "usuarios") // Nombre de la tabla en la BD
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String rol;

    // Puedes añadir constructores, getters/setters personalizados si los necesitas,
    // pero Lombok @Data @NoArgsConstructor @AllArgsConstructor ya cubren lo básico.
}