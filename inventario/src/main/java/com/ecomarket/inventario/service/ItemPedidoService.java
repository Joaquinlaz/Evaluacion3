package com.ecomarket.inventario.service;

import com.ecomarket.inventario.model.ItemPedido;
import java.util.List;

public interface ItemPedidoService {
    List<ItemPedido> findAll();
    ItemPedido findById(Long id);
    ItemPedido save(ItemPedido itemPedido);
    void deleteById(Long id);
}