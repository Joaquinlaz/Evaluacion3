package com.ecomarket.ecomarket.Controller;

import com.ecomarket.ecomarket.Model.Pedido;
import com.ecomarket.ecomarket.Service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.ecomarket.ecomarket.Hateoas.PedidoModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/pedidos")
public class PedidoController {
    private final PedidoService service;
    private final PedidoModelAssembler assembler;

    public PedidoController(PedidoService service, PedidoModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Obtener todos los pedidos")
    @GetMapping
    public CollectionModel<EntityModel<Pedido>> obtenerPedidos() {
        List<EntityModel<Pedido>> pedidos = service.obtenerPedidos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).obtenerPedidos()).withSelfRel());
    }

    @Operation(summary = "Crear un nuevo pedido")
    @PostMapping
    public EntityModel<Pedido> crearPedido(@RequestBody Pedido pedido) {
        Pedido creado = service.guardarPedido(pedido);
        return assembler.toModel(creado);
    }

    @Operation(summary = "Eliminar un pedido")
    @DeleteMapping("/{id}")
    public void eliminarPedido(@PathVariable Long id) {
        service.eliminarPedido(id);
    }
}
