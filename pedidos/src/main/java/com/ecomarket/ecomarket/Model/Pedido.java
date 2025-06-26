package com.ecomarket.ecomarket.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private int cantidad;
        private String cliente, producto;
        private double total;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        private LocalDateTime fecha;

}


