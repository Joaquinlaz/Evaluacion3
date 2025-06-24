package com.evaluacion2.Clientes.clientes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table (name = "Clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 10, nullable = false)
    private String run;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String address;

    @Column(nullable = true)
    private Date fechaNacimiento;

    @Column(nullable = false)
    private Date fechaRegistro;

    @Column(nullable = false)
    private String correo;

}
