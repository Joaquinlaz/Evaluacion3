package com.evaluacion2.envios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.evaluacion2.envios.model.Envios;
import com.evaluacion2.envios.service.EnviosService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/envios")
@Tag(name = "Envios", description = "Operaciones para la gestión de envíos")
public class EnviosController {
    @Autowired
    private EnviosService enviosService;

    @Operation(
            summary = "Crear un nuevo envío",
            description = "Crea un nuevo registro de envío.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Envío creado exitosamente", content = @Content(schema = @Schema(implementation = Envios.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
            }
    )
    @PostMapping
    public ResponseEntity<EntityModel<Envios>> createEnvio(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del envío a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Envios.class))
            )
            @RequestBody Envios envio) {
        Envios createdEnvio = enviosService.saveEnvio(envio);
        EntityModel<Envios> resource = EntityModel.of(createdEnvio,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnviosController.class).getEnvioById(createdEnvio.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnviosController.class).getAllEnvios()).withRel("todos-los-envios")
        );
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Obtener un envío por ID",
            description = "Obtiene los detalles de un envío específico por su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Envío encontrado", content = @Content(schema = @Schema(implementation = Envios.class))),
                    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Envios>> getEnvioById(
            @Parameter(description = "ID del envío", required = true) @PathVariable Long id) {
        Envios envio = enviosService.getEnvioById(id);
        if (envio != null) {
            EntityModel<Envios> resource = EntityModel.of(envio,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnviosController.class).getEnvioById(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnviosController.class).getAllEnvios()).withRel("todos-los-envios")
            );
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Obtener todos los envíos",
            description = "Obtiene una lista de todos los envíos registrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de envíos", content = @Content(schema = @Schema(implementation = Envios.class))),
                    @ApiResponse(responseCode = "204", description = "No hay envíos registrados")
            }
    )
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Envios>>> getAllEnvios() {
        List<Envios> envios = enviosService.getAllEnvios();
        if (envios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Envios>> enviosResources = envios.stream()
                .map(envio -> EntityModel.of(envio,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnviosController.class).getEnvioById(envio.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Envios>> collection = CollectionModel.of(enviosResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnviosController.class).getAllEnvios()).withSelfRel()
        );
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener envíos por fecha",
            description = "Obtiene una lista de envíos filtrados por fecha.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de envíos por fecha", content = @Content(schema = @Schema(implementation = Envios.class))),
                    @ApiResponse(responseCode = "204", description = "No hay envíos para la fecha indicada")
            }
    )
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<CollectionModel<EntityModel<Envios>>> getEnviosByFecha(
            @Parameter(description = "Fecha del envío", required = true) @PathVariable Date fecha) {
        List<Envios> envios = enviosService.findByFecha(fecha);
        if (envios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Envios>> enviosResources = envios.stream()
                .map(envio -> EntityModel.of(envio,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnviosController.class).getEnvioById(envio.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Envios>> collection = CollectionModel.of(enviosResources);
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener envíos por estado",
            description = "Obtiene una lista de envíos filtrados por estado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de envíos por estado", content = @Content(schema = @Schema(implementation = Envios.class))),
                    @ApiResponse(responseCode = "204", description = "No hay envíos para el estado indicado")
            }
    )
    @GetMapping("/estado/{estado}")
    public ResponseEntity<CollectionModel<EntityModel<Envios>>> getEnviosByEstado(
            @Parameter(description = "Estado del envío", required = true) @PathVariable String estado) {
        List<Envios> envios = enviosService.findByEstado(estado);
        if (envios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Envios>> enviosResources = envios.stream()
                .map(envio -> EntityModel.of(envio,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnviosController.class).getEnvioById(envio.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Envios>> collection = CollectionModel.of(enviosResources);
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(
            summary = "Actualizar un envío",
            description = "Actualiza todos los datos de un envío existente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Envío actualizado exitosamente", content = @Content(schema = @Schema(implementation = Envios.class))),
                    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Envios>> updateEnvio(
            @Parameter(description = "ID del envío", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del envío",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Envios.class))
            )
            @RequestBody Envios envio) {
        Envios existingEnvio = enviosService.getEnvioById(id);
        if (existingEnvio != null) {
            existingEnvio.setFechaEnvio(envio.getFechaEnvio());
            existingEnvio.setEstadoEnvio(envio.getEstadoEnvio());
            existingEnvio.setAddress(envio.getAddress());
            existingEnvio.setCiudad(envio.getCiudad());
            existingEnvio.setNombre(envio.getNombre());
            existingEnvio.setClienteId(envio.getClienteId());
            Envios updatedEnvio = enviosService.saveEnvio(existingEnvio);
            EntityModel<Envios> resource = EntityModel.of(updatedEnvio,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnviosController.class).getEnvioById(updatedEnvio.getId())).withSelfRel()
            );
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Eliminar un envío",
            description = "Elimina un envío existente por su ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Envío eliminado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnvio(
            @Parameter(description = "ID del envío", required = true) @PathVariable Long id) {
        enviosService.deleteEnvio(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Actualizar parcialmente un envío",
            description = "Actualiza parcialmente los datos de un envío existente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Envío actualizado exitosamente", content = @Content(schema = @Schema(implementation = Envios.class))),
                    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Envios>> patchEnvio(
            @Parameter(description = "ID del envío", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos a actualizar parcialmente del envío",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Envios.class))
            )
            @RequestBody Envios envio) {
        Envios existingEnvio = enviosService.getEnvioById(id);
        if (existingEnvio != null) {
            if (envio.getFechaEnvio() != null) {
                existingEnvio.setFechaEnvio(envio.getFechaEnvio());
            }
            if (envio.getEstadoEnvio() != null) {
                existingEnvio.setEstadoEnvio(envio.getEstadoEnvio());
            }
            if (envio.getAddress() != null) {
                existingEnvio.setAddress(envio.getAddress());
            }
            if (envio.getCiudad() != null) {
                existingEnvio.setCiudad(envio.getCiudad());
            }
            if (envio.getNombre() != null) {
                existingEnvio.setNombre(envio.getNombre());
            }
            if (envio.getClienteId() != null) {
                existingEnvio.setClienteId(envio.getClienteId());
            }
            Envios updatedEnvio = enviosService.saveEnvio(existingEnvio);
            EntityModel<Envios> resource = EntityModel.of(updatedEnvio,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnviosController.class).getEnvioById(updatedEnvio.getId())).withSelfRel()
            );
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}