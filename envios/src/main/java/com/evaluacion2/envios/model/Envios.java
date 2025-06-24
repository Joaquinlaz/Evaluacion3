package com.evaluacion2.envios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table (name = "Envios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Date fechaEnvio;

    @Column(nullable = false)
    private String estadoEnvio;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;
}
