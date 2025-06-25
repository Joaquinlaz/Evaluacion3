package com.ecomarket.ecomarket.Controller;

import com.ecomarket.ecomarket.Model.Pedido;
import com.ecomarket.ecomarket.Service.PedidoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    @Test
    @DisplayName("Debe retornar lista de pedidos")
    void testObtenerPedidos() {
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();

        when(pedidoService.obtenerPedidos()).thenReturn(Arrays.asList(pedido1, pedido2));

        List<Pedido> resultado = pedidoController.obtenerPedidos();

        assertEquals(2, resultado.size());
        verify(pedidoService, times(1)).obtenerPedidos();
    }

    @Test
    @DisplayName("Debe crear un nuevo pedido")
    void testCrearPedido() {
        Pedido nuevoPedido = new Pedido();

        when(pedidoService.guardarPedido(nuevoPedido)).thenReturn(nuevoPedido);

        Pedido resultado = pedidoController.crearPedido(nuevoPedido);

        assertEquals(nuevoPedido, resultado);
        verify(pedidoService, times(1)).guardarPedido(nuevoPedido);
    }

    @Test
    @DisplayName("Debe eliminar un pedido por ID")
    void testEliminarPedido() {
        Long id = 1L;
        pedidoController.eliminarPedido(id);

        verify(pedidoService, times(1)).eliminarPedido(id);
    }
}
