package com.evaluacion2.Clientes.clientes.controller;

import com.evaluacion2.Clientes.clientes.model.Cliente;
import com.evaluacion2.Clientes.clientes.service.CompraService;
import com.evaluacion2.Clientes.clientes.model.Compra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v2/compras")
@Tag(name = "Compras", description = "Operaciones para gestionar compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @Operation(summary = "Obtener todas las compras", description = "Obtiene una lista de todas las compras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compras encontradas"),
            @ApiResponse(responseCode = "204", description = "No se encontraron compras")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Compra>>> getAllCompras() {
        List<Compra> compras = compraService.getAllCompras();
        if (compras.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Compra>> compraResources = compras.stream()
                .map(compra -> EntityModel.of(compra,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getCompraById(compra.getId())).withSelfRel()))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Compra>> collection = CollectionModel.of(compraResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getAllCompras()).withSelfRel());
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Obtener compra por ID", description = "Obtiene una compra usando su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra encontrada"),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Compra>> getCompraById(
            @Parameter(description = "ID de la compra a buscar") @PathVariable Long id) {
        Compra compra = compraService.getCompraById(id);
        if (compra != null) {
            EntityModel<Compra> resource = EntityModel.of(compra,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getCompraById(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getAllCompras()).withRel("compras"));
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Obtener compras por ID de cliente", description = "Obtiene una lista de compras por el ID del cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compras encontradas"),
            @ApiResponse(responseCode = "204", description = "No se encontraron compras")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<CollectionModel<EntityModel<Compra>>> getComprasByClienteId(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId) {
        List<Compra> compras = compraService.findByClienteId(clienteId);
        if (compras.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Compra>> compraResources = compras.stream()
                .map(compra -> EntityModel.of(compra,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getCompraById(compra.getId())).withSelfRel()))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Compra>> collection = CollectionModel.of(compraResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getAllCompras()).withRel("compras"));
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Crear una compra", description = "Crea una nueva compra para un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Compra creada exitosamente")
    })
    @PostMapping("/{clienteId}")
    public ResponseEntity<EntityModel<Compra>> createCompra(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la compra a crear") @RequestBody Compra compra) {
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        compra.setCliente(cliente);
        Compra createdCompra = compraService.saveCompra(compra);
        EntityModel<Compra> resource = EntityModel.of(createdCompra,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getCompraById(createdCompra.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getAllCompras()).withRel("compras"));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar parcialmente una compra", description = "Actualiza parcialmente los datos de una compra existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Compra>> updateCompra(
            @Parameter(description = "ID de la compra a actualizar") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos a actualizar parcialmente") @RequestBody Compra compra) {
        Compra existingCompra = compraService.getCompraById(id);
        if (existingCompra != null) {
            existingCompra.setPrecioTotal(compra.getPrecioTotal());
            existingCompra.setFechaCompra(compra.getFechaCompra());
            Compra updatedCompra = compraService.saveCompra(existingCompra);
            EntityModel<Compra> resource = EntityModel.of(updatedCompra,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getCompraById(updatedCompra.getId())).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getAllCompras()).withRel("compras"));
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Buscar compras por rango de precio total", description = "Obtiene compras cuyo precio total está entre dos valores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compras encontradas"),
            @ApiResponse(responseCode = "204", description = "No se encontraron compras")
    })
    @GetMapping("/precioTotal")
    public ResponseEntity<CollectionModel<EntityModel<Compra>>> getComprasByPrecioTotal(
            @Parameter(description = "Precio mínimo") @RequestParam Double minPrice,
            @Parameter(description = "Precio máximo") @RequestParam Double maxPrice) {
        List<Compra> compras = compraService.findByPrecioTotalBetween(minPrice, maxPrice);
        if (compras.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Compra>> compraResources = compras.stream()
                .map(compra -> EntityModel.of(compra,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getCompraById(compra.getId())).withSelfRel()))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Compra>> collection = CollectionModel.of(compraResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getAllCompras()).withRel("compras"));
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Buscar compras por rango de fechas", description = "Obtiene compras realizadas entre dos fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compras encontradas"),
            @ApiResponse(responseCode = "204", description = "No se encontraron compras")
    })
    @GetMapping("/fechaCompra")
    public ResponseEntity<CollectionModel<EntityModel<Compra>>> getComprasByFechaCompra(
            @Parameter(description = "Fecha de inicio") @RequestParam Date startDate,
            @Parameter(description = "Fecha de término") @RequestParam Date endDate) {
        List<Compra> compras = compraService.findByFechaCompraBetween(startDate, endDate);
        if (compras.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Compra>> compraResources = compras.stream()
                .map(compra -> EntityModel.of(compra,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getCompraById(compra.getId())).withSelfRel()))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Compra>> collection = CollectionModel.of(compraResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CompraController.class).getAllCompras()).withRel("compras"));
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar compra", description = "Elimina una compra por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Compra eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompra(
            @Parameter(description = "ID de la compra a eliminar") @PathVariable Long id) {
        Compra existingCompra = compraService.getCompraById(id);
        if (existingCompra != null) {
            compraService.deleteCompra(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

