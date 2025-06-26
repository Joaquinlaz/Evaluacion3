package com.ecomarket.ecomarket.Controller;

import com.ecomarket.ecomarket.Model.Pedido;
import com.ecomarket.ecomarket.Service.PedidoService;
import com.ecomarket.ecomarket.Assembler.PedidoModelAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/pedidos")
public class PedidoController {

    private final PedidoService service;
    private final PedidoModelAssembler assembler;

    public PedidoController(PedidoService service, PedidoModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(
            summary = "Obtener todos los pedidos",
            description = "Devuelve una lista de todos los pedidos registrados en el sistema con enlaces HATEOAS."
    )
    @GetMapping
    public CollectionModel<EntityModel<Pedido>> obtenerPedidos() {
        List<EntityModel<Pedido>> pedidos = service.obtenerPedidos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).obtenerPedidos()).withSelfRel());
    }

    @Operation(
            summary = "Crear un nuevo pedido",
            description = "Crea un nuevo pedido basado en los datos proporcionados en el cuerpo de la solicitud."
    )
    @PostMapping
    public EntityModel<Pedido> crearPedido(
            @Parameter(description = "Detalles del pedido que se va a crear.")
            @RequestBody Pedido pedido) {
        Pedido creado = service.guardarPedido(pedido);
        return assembler.toModel(creado);
    }

    @Operation(
            summary = "Eliminar un pedido",
            description = "Elimina un pedido identificado por su ID."
    )
    @DeleteMapping("/{id}")
    public void eliminarPedido(
            @Parameter(description = "ID del pedido que se desea eliminar.")
            @PathVariable Long id) {
        service.eliminarPedido(id);
    }
}
