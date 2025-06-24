package com.evaluacion2.envios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.evaluacion2.envios.model.Ruta;
import com.evaluacion2.envios.service.RutaService;
import com.evaluacion2.envios.service.EnviosService;
import com.evaluacion2.envios.model.Envios;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/rutas")
@Tag(name = "Rutas", description = "Operaciones para la gestión de rutas")
public class RutaController {
    @Autowired
    private RutaService rutaService;
    @Autowired
    private EnviosService enviosService;

    @Operation(
            summary = "Crear una nueva ruta para un envío",
            description = "Crea una nueva ruta asociada a un envío existente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Ruta creada exitosamente", content = @Content(schema = @Schema(implementation = Ruta.class))),
                    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
            }
    )
    @PostMapping("/{envioId}")
    public ResponseEntity<EntityModel<Ruta>> createRuta(
            @Parameter(description = "ID del envío", required = true) @PathVariable Long envioId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la ruta a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Ruta.class))
            )
            @RequestBody Ruta ruta) {
        Envios envio = enviosService.getEnvioById(envioId);
        if (envio == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ruta.setEnvio(envio);
        Ruta createdRuta = rutaService.saveRuta(ruta);
        EntityModel<Ruta> resource = EntityModel.of(createdRuta,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RutaController.class).getRutaById(createdRuta.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RutaController.class).getAllRutas()).withRel("todas-las-rutas")
        );
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Obtener una ruta por ID",
            description = "Obtiene los detalles de una ruta específica por su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ruta encontrada", content = @Content(schema = @Schema(implementation = Ruta.class))),
                    @ApiResponse(responseCode = "404", description = "Ruta no encontrada")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Ruta>> getRutaById(
            @Parameter(description = "ID de la ruta", required = true) @PathVariable Long id) {
        Ruta ruta = rutaService.getRutaById(id);
        if (ruta != null) {
            EntityModel<Ruta> resource = EntityModel.of(ruta,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RutaController.class).getRutaById(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RutaController.class).getAllRutas()).withRel("todas-las-rutas")
            );
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Obtener rutas por comuna de origen",
            description = "Obtiene una lista de rutas filtradas por comuna de origen.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de rutas por comuna", content = @Content(schema = @Schema(implementation = Ruta.class))),
                    @ApiResponse(responseCode = "204", description = "No hay rutas para la comuna indicada")
            }
    )
    @GetMapping("/comuna/{comuna}")
    public ResponseEntity<CollectionModel<EntityModel<Ruta>>> getRutasByCiudad(
            @Parameter(description = "Comuna de origen", required = true) @PathVariable String comuna) {
        List<Ruta> rutas = rutaService.findByComunaOrigen(comuna);
        if (rutas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Ruta>> rutasResources = rutas.stream()
                .map(ruta -> EntityModel.of(ruta,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RutaController.class).getRutaById(ruta.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Ruta>> collection = CollectionModel.of(rutasResources);
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener todas las rutas",
            description = "Obtiene una lista de todas las rutas registradas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de rutas", content = @Content(schema = @Schema(implementation = Ruta.class))),
                    @ApiResponse(responseCode = "204", description = "No hay rutas registradas")
            }
    )
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Ruta>>> getAllRutas() {
        List<Ruta> rutas = rutaService.getAllRutas();
        if (rutas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Ruta>> rutasResources = rutas.stream()
                .map(ruta -> EntityModel.of(ruta,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RutaController.class).getRutaById(ruta.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Ruta>> collection = CollectionModel.of(rutasResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RutaController.class).getAllRutas()).withSelfRel()
        );
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(
            summary = "Actualizar parcialmente una ruta",
            description = "Actualiza parcialmente los datos de una ruta existente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ruta actualizada exitosamente", content = @Content(schema = @Schema(implementation = Ruta.class))),
                    @ApiResponse(responseCode = "404", description = "Ruta no encontrada")
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Ruta>> updateRuta(
            @Parameter(description = "ID de la ruta", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos a actualizar parcialmente de la ruta",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Ruta.class))
            )
            @RequestBody Ruta ruta) {
        Ruta existingRuta = rutaService.getRutaById(id);
        if (existingRuta != null) {
            if (ruta.getNombreRuta() != null) {
                existingRuta.setNombreRuta(ruta.getNombreRuta());
            }
            if (ruta.getDireccionDestino() != null) {
                existingRuta.setDireccionDestino(ruta.getDireccionDestino());
            }
            if (ruta.getComunaOrigen() != null) {
                existingRuta.setComunaOrigen(ruta.getComunaOrigen());
            }
            if (ruta.getComunaDestino() != null) {
                existingRuta.setComunaDestino(ruta.getComunaDestino());
            }
            if (ruta.getEnvio() != null) {
                existingRuta.setEnvio(ruta.getEnvio());
            }
            Ruta updatedRuta = rutaService.saveRuta(existingRuta);
            EntityModel<Ruta> resource = EntityModel.of(updatedRuta,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RutaController.class).getRutaById(updatedRuta.getId())).withSelfRel()
            );
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Eliminar una ruta",
            description = "Elimina una ruta existente por su ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Ruta eliminada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Ruta no encontrada")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRuta(
            @Parameter(description = "ID de la ruta", required = true) @PathVariable Long id) {
        rutaService.deleteRuta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Actualizar una ruta",
            description = "Actualiza todos los datos de una ruta existente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ruta actualizada exitosamente", content = @Content(schema = @Schema(implementation = Ruta.class))),
                    @ApiResponse(responseCode = "404", description = "Ruta no encontrada")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Ruta>> partialUpdateRuta(
            @Parameter(description = "ID de la ruta", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la ruta",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Ruta.class))
            )
            @RequestBody Ruta ruta) {
        Ruta existingRuta = rutaService.getRutaById(id);
        if (existingRuta != null) {
            existingRuta.setNombreRuta(ruta.getNombreRuta());
            existingRuta.setDireccionDestino(ruta.getDireccionDestino());
            existingRuta.setComunaOrigen(ruta.getComunaOrigen());
            existingRuta.setComunaDestino(ruta.getComunaDestino());
            existingRuta.setEnvio(ruta.getEnvio());
            Ruta updatedRuta = rutaService.saveRuta(existingRuta);
            EntityModel<Ruta> resource = EntityModel.of(updatedRuta,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RutaController.class).getRutaById(updatedRuta.getId())).withSelfRel()
            );
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
