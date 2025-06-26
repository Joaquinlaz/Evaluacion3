package com.ecomarket.usuarios_nuevo.controller;

import com.ecomarket.usuarios_nuevo.model.Usuario;
import com.ecomarket.usuarios_nuevo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones CRUD para usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Devuelve una lista de todos los usuarios registrados con enlaces HATEOAS."
    )
    @GetMapping
    public CollectionModel<EntityModel<Usuario>> obtenerTodos() {
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        List<EntityModel<Usuario>> modelos = usuarios.stream().map(usuario ->
                EntityModel.of(usuario,
                        linkTo(methodOn(UsuarioController.class).obtenerPorId(usuario.getId())).withSelfRel(),
                        linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("usuarios")
                )
        ).toList();
        return CollectionModel.of(modelos, linkTo(methodOn(UsuarioController.class).obtenerTodos()).withSelfRel());
    }

    @Operation(
        summary = "Obtener un usuario por ID",
        description = "Devuelve los detalles de un usuario específico junto con enlaces HATEOAS."
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) return ResponseEntity.notFound().build();

        EntityModel<Usuario> recurso = EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerPorId(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("usuarios")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(
        summary = "Crear un nuevo usuario",
        description = "Crea un nuevo usuario con los datos proporcionados en el cuerpo de la solicitud."
    )
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> crear(@RequestBody Usuario usuario) {
        Usuario creado = usuarioService.crear(usuario);
        EntityModel<Usuario> recurso = EntityModel.of(creado,
                linkTo(methodOn(UsuarioController.class).obtenerPorId(creado.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("usuarios")
        );
        return ResponseEntity
                .created(linkTo(methodOn(UsuarioController.class).obtenerPorId(creado.getId())).toUri())
                .body(recurso);
    }

    @Operation(
        summary = "Actualizar un usuario",
        description = "Actualiza los datos de un usuario existente basado en su ID."
    )
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.actualizar(id, usuario);
        if (actualizado == null) return ResponseEntity.notFound().build();

        EntityModel<Usuario> recurso = EntityModel.of(actualizado,
                linkTo(methodOn(UsuarioController.class).obtenerPorId(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).obtenerTodos()).withRel("usuarios")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(
        summary = "Eliminar un usuario",
        description = "Elimina un usuario identificado por su ID."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
