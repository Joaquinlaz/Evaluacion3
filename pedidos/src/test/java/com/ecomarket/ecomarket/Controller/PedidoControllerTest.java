package com.ecomarket.ecomarket.Controller;

import com.ecomarket.ecomarket.Model.Pedido;
import com.ecomarket.ecomarket.Service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pedido pedido1;
    private Pedido pedido2;

    @BeforeEach
    void setUp() {
        pedido1 = new Pedido();
        pedido1.setId(1L);
        pedido1.setCantidad(3);
        pedido1.setCliente("Cliente 1");
        pedido1.setProducto("Producto A");
        pedido1.setTotal(4500.0);
        pedido1.setFecha(LocalDateTime.of(2025, 6, 24, 10, 0));

        pedido2 = new Pedido();
        pedido2.setId(2L);
        pedido2.setCantidad(5);
        pedido2.setCliente("Cliente 2");
        pedido2.setProducto("Producto B");
        pedido2.setTotal(7500.0);
        pedido2.setFecha(LocalDateTime.of(2025, 6, 25, 12, 0));
    }

    @Test
    void obtenerPedidos_debeRetornarListaDePedidos() throws Exception {
        List<Pedido> pedidos = Arrays.asList(pedido1, pedido2);
        Mockito.when(pedidoService.obtenerPedidos()).thenReturn(pedidos);

        mockMvc.perform(get("/api/v2/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(pedidos.size()))
                .andExpect(jsonPath("$[0].cliente").value("Cliente 1"))
                .andExpect(jsonPath("$[1].producto").value("Producto B"));
    }

    @Test
    void crearPedido_debeRetornarPedidoGuardado() throws Exception {
        Mockito.when(pedidoService.guardarPedido(any(Pedido.class))).thenReturn(pedido1);

        mockMvc.perform(post("/api/v2/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente").value("Cliente 1"))
                .andExpect(jsonPath("$.cantidad").value(3))
                .andExpect(jsonPath("$.total").value(4500.0));
    }

    @Test
    void eliminarPedido_debeRetornarOk() throws Exception {
        Long id = 1L;
        Mockito.doNothing().when(pedidoService).eliminarPedido(id);

        mockMvc.perform(delete("/api/v2/pedidos/{id}", id))
                .andExpect(status().isOk());
    }
}
