package com.ecomarket.inventario.controller;

import com.ecomarket.inventario.model.ItemPedido;
import com.ecomarket.inventario.service.ItemPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items-pedido")
public class ItemPedidoController {

    private final ItemPedidoService itemPedidoService;

    @Autowired
    public ItemPedidoController(ItemPedidoService itemPedidoService) {
        this.itemPedidoService = itemPedidoService;
    }

    @GetMapping
    public List<ItemPedido> getAllItemsPedido() {
        return itemPedidoService.findAll();
    }

    @GetMapping("/{id}")
    public ItemPedido getItemPedidoById(@PathVariable Long id) {
        return itemPedidoService.findById(id);
    }

    @PostMapping
    public ItemPedido createItemPedido(@RequestBody ItemPedido itemPedido) {
        return itemPedidoService.save(itemPedido);
    }

    @DeleteMapping("/{id}")
    public void deleteItemPedido(@PathVariable Long id) {
        itemPedidoService.deleteById(id);
    }
}