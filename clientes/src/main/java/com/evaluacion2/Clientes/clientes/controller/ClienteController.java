package com.evaluacion2.Clientes.clientes.controller;

import com.evaluacion2.Clientes.clientes.model.Cliente;
import com.evaluacion2.Clientes.clientes.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/api/v2/clientes")
@Tag(name = "Clientes", description = "Operaciones para gestionar clientes")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Crear un nuevo cliente", description = "Crea un cliente nuevo en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Cliente>> createCliente(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del cliente a crear")
            @RequestBody Cliente cliente) {
        Cliente createdCliente = clienteService.saveCliente(cliente);
        EntityModel<Cliente> resource = EntityModel.of(createdCliente,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getClienteById(createdCliente.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getAllClientes()).withRel("clientes"));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener cliente por ID", description = "Obtiene un cliente usando su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> getClienteById(
            @Parameter(description = "ID del cliente a buscar") @PathVariable Long id) {
        Cliente cliente = clienteService.getClienteById(id);
        if (cliente != null) {
            EntityModel<Cliente> resource = EntityModel.of(cliente,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getClienteById(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getAllClientes()).withRel("clientes"));
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Buscar clientes por nombre de usuario", description = "Obtiene una lista de clientes por su nombre de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron clientes")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> getClientesByUsername(
            @Parameter(description = "Nombre de usuario a buscar") @PathVariable String username) {
        List<Cliente> clientes = clienteService.findByUsername(username);
        if (clientes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Cliente>> clienteResources = clientes.stream()
                .map(cliente -> EntityModel.of(cliente,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getClienteById(cliente.getId())).withSelfRel()))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Cliente>> collection = CollectionModel.of(clienteResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getAllClientes()).withRel("clientes"));
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Buscar clientes por ciudad", description = "Obtiene una lista de clientes por ciudad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron clientes")
    })
    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> getClientesByCiudad(
            @Parameter(description = "Ciudad a buscar") @PathVariable String ciudad) {
        List<Cliente> clientes = clienteService.findByCiudad(ciudad);
        if (clientes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Cliente>> clienteResources = clientes.stream()
                .map(cliente -> EntityModel.of(cliente,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getClienteById(cliente.getId())).withSelfRel()))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Cliente>> collection = CollectionModel.of(clienteResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getAllClientes()).withRel("clientes"));
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Buscar clientes por rango de fechas de registro", description = "Obtiene clientes registrados entre dos fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron clientes")
    })
    @GetMapping("/fechaRegistro")
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> getClientesByFechaRegistro
            (@Parameter(description = "Fecha de inicio (dd-MM-yyyy)") @RequestParam @DateTimeFormat (pattern = "dd-MM-yyyy") Date startDate,
             @Parameter(description = "Fecha de término (dd-MM-yyyy)") @RequestParam @DateTimeFormat (pattern ="dd-MM-yyyy") Date endDate) {
        List<Cliente> clientes = clienteService.findByFechaRegistroBetween(startDate, endDate);
        if (clientes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Cliente>> clienteResources = clientes.stream()
                .map(cliente -> EntityModel.of(cliente,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getClienteById(cliente.getId())).withSelfRel()))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Cliente>> collection = CollectionModel.of(clienteResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getAllClientes()).withRel("clientes"));
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Obtener todos los clientes", description = "Obtiene una lista de todos los clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron clientes")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> getAllClientes() {
        List<Cliente> clientes = clienteService.getAllClientes();
        if (clientes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<Cliente>> clienteResources = clientes.stream()
                .map(cliente -> EntityModel.of(cliente,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getClienteById(cliente.getId())).withSelfRel()))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<Cliente>> collection = CollectionModel.of(clienteResources,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getAllClientes()).withSelfRel());
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar cliente", description = "Actualiza todos los datos de un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> updateCliente(
            @Parameter(description = "ID del cliente a actualizar") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del cliente")
            @RequestBody Cliente cliente) {
        cliente.setId(id);
        cliente.setRun(cliente.getRun());
        cliente.setUsername(cliente.getUsername());
        cliente.setFechaRegistro(cliente.getFechaRegistro());
        cliente.setFechaNacimiento(cliente.getFechaNacimiento());
        cliente.setCiudad(cliente.getCiudad());
        cliente.setCorreo(cliente.getCorreo());
        cliente.setAddress(cliente.getAddress());
        Cliente updatedCliente = clienteService.saveCliente(cliente);
        EntityModel<Cliente> resource = EntityModel.of(updatedCliente,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getClienteById(updatedCliente.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getAllClientes()).withRel("clientes"));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @Operation(summary = "Actualizar parcialmente un cliente", description = "Actualiza parcialmente los datos de un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> partialUpdateCliente(
            @Parameter(description = "ID del cliente a actualizar") @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos a actualizar parcialmente")
            @RequestBody Cliente cliente) {
        Cliente existingCliente = clienteService.getClienteById(id);
        if (existingCliente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (cliente.getUsername() != null) {
            existingCliente.setUsername(cliente.getUsername());
        }
        if (cliente.getCiudad() != null) {
            existingCliente.setCiudad(cliente.getCiudad());
        }
        if (cliente.getRun() != null) {
            existingCliente.setRun(cliente.getRun());
        }
        if (cliente.getCorreo() != null) {
            existingCliente.setCorreo(cliente.getCorreo());
        }
        if (cliente.getFechaRegistro() != null) {
            existingCliente.setFechaRegistro(cliente.getFechaRegistro());
        }
        if (cliente.getAddress() != null) {
            existingCliente.setAddress(cliente.getAddress());
        }
        if (cliente.getFechaNacimiento() != null) {
            existingCliente.setFechaNacimiento(cliente.getFechaNacimiento());
        }
        Cliente updatedCliente = clienteService.saveCliente(existingCliente);
        EntityModel<Cliente> resource = EntityModel.of(updatedCliente,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getClienteById(updatedCliente.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteController.class).getAllClientes()).withRel("clientes"));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(
            @Parameter(description = "ID del cliente a eliminar") @PathVariable Long id) {
        clienteService.deleteCliente(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}