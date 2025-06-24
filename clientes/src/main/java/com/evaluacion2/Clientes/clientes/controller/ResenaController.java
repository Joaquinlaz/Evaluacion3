package com.evaluacion2.Clientes.clientes.controller;

import com.evaluacion2.Clientes.clientes.model.Cliente;
import com.evaluacion2.Clientes.clientes.service.ResenaService;
import com.evaluacion2.Clientes.clientes.model.Resena;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.evaluacion2.Clientes.clientes.service.ClienteService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

// Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/resenas")
@Tag(name = "Reseñas", description = "Operaciones para gestionar reseñas")
public class ResenaController {
    @Autowired
    private ResenaService resenaService;
    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Obtener todas las reseñas", description = "Obtiene una lista de todas las reseñas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reseñas encontradas"),
            @ApiResponse(responseCode = "204", description = "No se encontraron reseñas")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Resena>>> getAllResenas() {
        List<Resena> resenas = resenaService.getAllResenas();
        if (resenas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Resena>> resenaResources = resenas.stream()
                .map(resena -> EntityModel.of(resena,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ResenaController.class).getResenaById(resena.getId())).withSelfRel()))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Resena>> collection = CollectionModel.of(resenaResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ResenaController.class).getAllResenas()).withSelfRel());
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Obtener reseña por ID", description = "Obtiene una reseña usando su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Resena>> getResenaById(
            @Parameter(description = "ID de la reseña a buscar") @PathVariable Long id) {
        Resena resena = resenaService.getResenaById(id);
        if (resena != null) {
            EntityModel<Resena> resource = EntityModel.of(resena,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ResenaController.class).getResenaById(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ResenaController.class).getAllResenas()).withRel("resenas"));
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Obtener reseña por rating", description = "Obtiene una reseña usando su rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reseña encontrada"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @GetMapping("/rating/{rating}")
    public ResponseEntity<EntityModel<Resena>> getResenaByRating(
            @Parameter(description = "Rating de la reseña a buscar") @PathVariable int rating) {
        Resena resena = resenaService.getResenaByRating(rating);
        if (resena != null) {
            EntityModel<Resena> resource = EntityModel.of(resena,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ResenaController.class).getResenaById(resena.getId())).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ResenaController.class).getAllResenas()).withRel("resenas"));
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Crear una reseña", description = "Crea una nueva reseña para un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PostMapping("/{clienteId}")
    public ResponseEntity<EntityModel<Resena>> createResena(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la reseña a crear") @RequestBody Resena resena) {
        Cliente cliente = clienteService.getClienteById(clienteId);
        if (cliente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        resena.setCliente(cliente);
        Resena createdResena = resenaService.saveResena(resena);
        EntityModel<Resena> resource = EntityModel.of(createdResena,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ResenaController.class).getResenaById(createdResena.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ResenaController.class).getAllResenas()).withRel("resenas"));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar parcialmente una reseña", description = "Actualiza parcialmente los datos de una reseña existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reseña actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reseña o cliente no encontrado")
    })
    @PatchMapping("/{clienteId}/{id}")
    public ResponseEntity<EntityModel<Resena>> updateResena(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId,
            @Parameter(description = "ID de la reseña a actualizar") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos a actualizar parcialmente") @RequestBody Resena resena) {
        Resena existingResena = resenaService.getResenaById(id);
        if (existingResena != null) {
            existingResena.setComentario(resena.getComentario());
            existingResena.setRating(resena.getRating());
            Cliente cliente = clienteService.getClienteById(clienteId);
            if (cliente == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            existingResena.setCliente(cliente);
            Resena updatedResena = resenaService.saveResena(existingResena);
            EntityModel<Resena> resource = EntityModel.of(updatedResena,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ResenaController.class).getResenaById(updatedResena.getId())).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ResenaController.class).getAllResenas()).withRel("resenas"));
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar reseña", description = "Elimina una reseña por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reseña eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResena(
            @Parameter(description = "ID de la reseña a eliminar") @PathVariable Long id) {
        Resena existingResena = resenaService.getResenaById(id);
        if (existingResena != null) {
            resenaService.deleteResena(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}