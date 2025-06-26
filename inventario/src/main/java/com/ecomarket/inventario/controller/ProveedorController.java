package com.ecomarket.inventario.controller;

import com.ecomarket.inventario.model.Proveedor;
import com.ecomarket.inventario.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@Tag(name = "Proveedores", description = "Operaciones CRUD para la entidad Proveedor")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @Operation(summary = "Listar todos los proveedores")
    @GetMapping
    public List<Proveedor> obtenerTodos() {
        return proveedorService.findAll();
    }

    @Operation(summary = "Crear un nuevo proveedor")
    @PostMapping
    public Proveedor crearProveedor(@RequestBody Proveedor proveedor) {
        return proveedorService.save(proveedor);
    }

    @Operation(summary = "Actualizar un proveedor existente")
    @PutMapping("/{id}")
    public Proveedor actualizarProveedor(@PathVariable Long id, @RequestBody Proveedor proveedorActualizado) {
        Proveedor proveedor = proveedorService.findById(id);
        if (proveedor != null) {
            proveedor.setNombre(proveedorActualizado.getNombre());
            proveedor.setContacto(proveedorActualizado.getContacto());
            proveedor.setDireccion(proveedorActualizado.getDireccion());
            proveedor.setTelefono(proveedorActualizado.getTelefono());
            return proveedorService.save(proveedor);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado con ID: " + id);
        }
    }

    @Operation(summary = "Eliminar un proveedor por ID")
    @DeleteMapping("/{id}")
    public void eliminarProveedor(@PathVariable Long id) {
        proveedorService.deleteById(id);
    }
}
