package com.evaluacion2.Clientes.clientes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table (name = "Soporte")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Soporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false)
    private String mensaje;

    @Column(nullable = false)
    private Date fechaCreacion;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String respuesta;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}
