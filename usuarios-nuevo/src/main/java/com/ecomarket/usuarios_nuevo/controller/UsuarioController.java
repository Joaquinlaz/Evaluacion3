package com.ecomarket.usuarios_nuevo.controller; // ¡PAQUETE CORRECTO!

import com.ecomarket.usuarios_nuevo.model.Usuario;
import com.ecomarket.usuarios_nuevo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder; // Necesario para linkTo y methodOn
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Helper method to convert a Usuario object to an EntityModel with HATEOAS links.
     * Método auxiliar para convertir un objeto Usuario a un EntityModel con enlaces HATEOAS.
     */
    private EntityModel<Usuario> toModel(Usuario usuario) {
        // Enlace "self" para el recurso individual
        WebMvcLinkBuilder selfLinkBuilder = linkTo(methodOn(UsuarioController.class).obtenerPorId(usuario.getId()));
        // Enlace "usuarios" para la colección de usuarios
        WebMvcLinkBuilder collectionLinkBuilder = linkTo(methodOn(UsuarioController.class).obtenerTodos());

        return EntityModel.of(usuario,
                selfLinkBuilder.withSelfRel(),
                collectionLinkBuilder.withRel("usuarios"));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> obtenerTodos() {
        List<EntityModel<Usuario>> usuarios = usuarioService.obtenerTodos().stream()
                .map(this::toModel) // Convierte cada Usuario a EntityModel con sus enlaces
                .collect(Collectors.toList());

        // Enlace "self" para la colección completa de usuarios
        CollectionModel<EntityModel<Usuario>> collectionModel = CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).obtenerTodos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerPorId(@PathVariable Long id) {
        // El servicio lanzará una excepción si el usuario no se encuentra
        Usuario usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(toModel(usuario));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.crear(usuario);
        EntityModel<Usuario> usuarioModel = toModel(nuevoUsuario);

        // Construir la URI para la cabecera 'Location' de la respuesta 201 Created
        URI location = usuarioModel.getRequiredLink("self").toUri(); // Obtiene la URI del enlace 'self'

        return ResponseEntity.created(location).body(usuarioModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        // El servicio se encarga de la lógica de actualización y de manejar si no existe
        Usuario usuarioActualizado = usuarioService.actualizar(id, usuario);
        return ResponseEntity.ok(toModel(usuarioActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id); // El servicio se encarga de eliminar o lanzar excepción
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content para indicar éxito sin cuerpo
    }
}