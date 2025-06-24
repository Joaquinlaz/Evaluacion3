package com.evaluacion2.Clientes.clientes.controller;

import com.evaluacion2.Clientes.clientes.model.Soporte;
import com.evaluacion2.Clientes.clientes.service.SoporteService;
import com.evaluacion2.Clientes.clientes.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

// Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/soporte")
@Tag(name = "Soporte", description = "Operaciones para gestionar solicitudes de soporte")
public class SoporteController {
    @Autowired
    private SoporteService soporteService;

    @Operation(summary = "Crear solicitud de soporte", description = "Crea una nueva solicitud de soporte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Soporte creado exitosamente")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Soporte>> createSoporte(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del soporte a crear") @RequestBody Soporte soporte) {
        Soporte createdSoporte = soporteService.saveSoporte(soporte);
        EntityModel<Soporte> resource = EntityModel.of(createdSoporte,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SoporteController.class).getSoporteById(createdSoporte.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SoporteController.class).getAllSoportes()).withRel("soportes"));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener soporte por ID", description = "Obtiene una solicitud de soporte usando su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Soporte encontrado"),
            @ApiResponse(responseCode = "404", description = "Soporte no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Soporte>> getSoporteById(
            @Parameter(description = "ID del soporte a buscar") @PathVariable Long id) {
        Soporte soporte = soporteService.getSoporteById(id);
        if (soporte != null) {
            EntityModel<Soporte> resource = EntityModel.of(soporte,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SoporteController.class).getSoporteById(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SoporteController.class).getAllSoportes()).withRel("soportes"));
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Obtener todas las solicitudes de soporte", description = "Obtiene una lista de todas las solicitudes de soporte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Soportes encontrados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron soportes")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Soporte>>> getAllSoportes() {
        List<Soporte> soportes = soporteService.getAllSoportes();
        if (soportes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Soporte>> soporteResources = soportes.stream()
                .map(soporte -> EntityModel.of(soporte,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SoporteController.class).getSoporteById(soporte.getId())).withSelfRel()))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Soporte>> collection = CollectionModel.of(soporteResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SoporteController.class).getAllSoportes()).withSelfRel());
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar soporte", description = "Elimina una solicitud de soporte por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Soporte eliminado exitosamente")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoporte(
            @Parameter(description = "ID del soporte a eliminar") @PathVariable Long id) {
        soporteService.deleteSoporte(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Obtener soportes por estado", description = "Obtiene una lista de soportes por su estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Soportes encontrados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron soportes")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<CollectionModel<EntityModel<Soporte>>> getSoportesByEstado(
            @Parameter(description = "Estado del soporte") @PathVariable String estado) {
        List<Soporte> soportes = soporteService.findByEstado(estado);
        if (soportes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Soporte>> soporteResources = soportes.stream()
                .map(soporte -> EntityModel.of(soporte,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SoporteController.class).getSoporteById(soporte.getId())).withSelfRel()))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Soporte>> collection = CollectionModel.of(soporteResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SoporteController.class).getAllSoportes()).withRel("soportes"));
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Obtener soportes por cliente", description = "Obtiene una lista de soportes por cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Soportes encontrados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron soportes")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<CollectionModel<EntityModel<Soporte>>> getSoportesByCliente(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId) {
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        List<Soporte> soportes = soporteService.findByCliente(cliente);
        if (soportes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Soporte>> soporteResources = soportes.stream()
                .map(soporte -> EntityModel.of(soporte,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SoporteController.class).getSoporteById(soporte.getId())).withSelfRel()))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Soporte>> collection = CollectionModel.of(soporteResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SoporteController.class).getAllSoportes()).withRel("soportes"));
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Obtener soportes por rango de fechas de creación", description = "Obtiene soportes creados entre dos fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Soportes encontrados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron soportes")
    })
    @GetMapping("/fechaCreacion")
    public ResponseEntity<CollectionModel<EntityModel<Soporte>>> getSoportesByFechaCreacion(
            @Parameter(description = "Fecha de inicio") @RequestParam Date startDate,
            @Parameter(description = "Fecha de término") @RequestParam Date endDate) {
        List<Soporte> soportes = soporteService.findByFechaCreacionBetween(startDate, endDate);
        if (soportes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Soporte>> soporteResources = soportes.stream()
                .map(soporte -> EntityModel.of(soporte,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SoporteController.class).getSoporteById(soporte.getId())).withSelfRel()))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Soporte>> collection = CollectionModel.of(soporteResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SoporteController.class).getAllSoportes()).withRel("soportes"));
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }
}