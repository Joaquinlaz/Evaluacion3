package com.ecomarket.inventario.service;

import com.ecomarket.inventario.model.Categoria;
import java.util.List;

public interface CategoriaService {
    List<Categoria> findAll();
    Categoria findById(Long id);
    Categoria save(Categoria categoria);
    void deleteById(Long id);
}