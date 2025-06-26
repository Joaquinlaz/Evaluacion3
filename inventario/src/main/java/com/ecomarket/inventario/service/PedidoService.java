package com.ecomarket.inventario.service;

import com.ecomarket.inventario.model.Pedido;
import java.util.List;

public interface PedidoService {
    List<Pedido> findAll();
    Pedido findById(Long id);
    Pedido save(Pedido pedido);
    void deleteById(Long id);
}