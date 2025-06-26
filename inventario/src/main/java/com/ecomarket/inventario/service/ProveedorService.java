package com.ecomarket.inventario.service;

import com.ecomarket.inventario.model.Proveedor;
import java.util.List;

public interface ProveedorService {
    List<Proveedor> findAll();
    Proveedor findById(Long id);
    Proveedor save(Proveedor proveedor);
    void deleteById(Long id);
}