package com.ecomarket.inventario.service.impl;

import com.ecomarket.inventario.model.ItemPedido;
import com.ecomarket.inventario.repository.ItemPedidoRepository;
import com.ecomarket.inventario.service.ItemPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemPedidoServiceImpl implements ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;

    @Autowired
    public ItemPedidoServiceImpl(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Override
    public List<ItemPedido> findAll() {
        return itemPedidoRepository.findAll();
    }

    @Override
    public ItemPedido findById(Long id) {
        return itemPedidoRepository.findById(id).orElse(null);
    }

    @Override
    public ItemPedido save(ItemPedido itemPedido) {
        return itemPedidoRepository.save(itemPedido);
    }

    @Override
    public void deleteById(Long id) {
        itemPedidoRepository.deleteById(id);
    }
}