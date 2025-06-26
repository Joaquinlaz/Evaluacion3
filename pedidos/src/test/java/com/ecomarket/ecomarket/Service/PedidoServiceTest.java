package com.ecomarket.ecomarket.Service;

import com.ecomarket.ecomarket.Model.Pedido;
import com.ecomarket.ecomarket.Repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
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
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
     
    }

    @Test
    @DisplayName("Debe retornar todos los pedidos")
    void testObtenerPedidos() {
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(pedido1, pedido2));

        List<Pedido> pedidos = pedidoService.obtenerPedidos();

        assertEquals(2, pedidos.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe guardar un pedido")
    void testGuardarPedido() {
        Pedido pedido = new Pedido();
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        Pedido resultado = pedidoService.guardarPedido(pedido);

        assertEquals(pedido, resultado);
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    @DisplayName("Debe eliminar un pedido por ID")
    void testEliminarPedido() {
        Long id = 1L;

        pedidoService.eliminarPedido(id);

        verify(pedidoRepository, times(1)).deleteById(id);
    }
}
