package com.evaluacion2.envios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table (name = "Ruta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreRuta;

    @Column(nullable = false)
    private String comunaDestino;

    @Column (nullable = false)
    private String comunaOrigen;

    @Column(nullable = false)
    private String direccionDestino;

    @ManyToOne
    @JoinColumn(name = "envio_id", nullable = false)
    private Envios envio;
}
