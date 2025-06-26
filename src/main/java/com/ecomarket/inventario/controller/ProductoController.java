package com.ecomarket.inventario.controller;

import com.ecomarket.inventario.model.Producto;
import com.ecomarket.inventario.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public CollectionModel<EntityModel<Producto>> obtenerTodos() {
        List<EntityModel<Producto>> productos = productoService.findAll().stream()
                .map(producto -> EntityModel.of(producto,
                        linkTo(methodOn(ProductoController.class).obtenerPorId(producto.getId())).withSelfRel(),
                        linkTo(methodOn(ProductoController.class).obtenerTodos()).withRel("productos")))
                .collect(Collectors.toList());

        return CollectionModel.of(productos, linkTo(methodOn(ProductoController.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Producto> obtenerPorId(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtenerPorId(id)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).obtenerTodos()).withRel("productos"));
    }

    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return productoService.save(producto);
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto actualizado) {
        Producto producto = productoService.findById(id);
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }

        producto.setNombre(actualizado.getNombre());
        producto.setUnidad(actualizado.getUnidad());
        producto.setStock(actualizado.getStock());
        producto.setPrecio(actualizado.getPrecio());

        return productoService.save(producto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        productoService.deleteById(id);
    }
}
