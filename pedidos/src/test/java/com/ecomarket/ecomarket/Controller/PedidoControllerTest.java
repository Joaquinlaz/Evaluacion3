package com.ecomarket.ecomarket.Controller;

import com.ecomarket.ecomarket.Model.Pedido;
import com.ecomarket.ecomarket.Service.PedidoService;
import com.ecomarket.ecomarket.Assembler.PedidoModelAssembler;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @MockBean
    private PedidoModelAssembler pedidoModelAssembler;

    @Test
    void obtenerPedidos_debeRetornarPedidosConLinks() throws Exception {
        LocalDateTime ahora = LocalDateTime.of(2024, 6, 1, 10, 30);

        Pedido pedido1 = Pedido.builder()
                .id(1L)
                .cantidad(2)
                .cliente("Cliente 1")
                .producto("Producto A")
                .total(5000.0)
                .fecha(ahora)
                .build();

        Pedido pedido2 = Pedido.builder()
                .id(2L)
                .cantidad(1)
                .cliente("Cliente 2")
                .producto("Producto B")
                .total(2500.0)
                .fecha(ahora)
                .build();

        List<Pedido> pedidos = Arrays.asList(pedido1, pedido2);

        Mockito.when(pedidoService.obtenerPedidos()).thenReturn(pedidos);

        Mockito.when(pedidoModelAssembler.toModel(pedido1))
                .thenReturn(EntityModel.of(pedido1,
                        linkTo(methodOn(PedidoController.class).obtenerPedidos()).withRel("todos-los-pedidos"),
                        linkTo(PedidoController.class).slash(pedido1.getId()).withRel("eliminar")));

        Mockito.when(pedidoModelAssembler.toModel(pedido2))
                .thenReturn(EntityModel.of(pedido2,
                        linkTo(methodOn(PedidoController.class).obtenerPedidos()).withRel("todos-los-pedidos"),
                        linkTo(PedidoController.class).slash(pedido2.getId()).withRel("eliminar")));

        mockMvc.perform(get("/api/v2/pedidos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Mostrar JSON completo por consola (opcional para depurar)
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                // Validar estructura principal
                .andExpect(jsonPath("$._embedded.pedidoList").exists())
                .andExpect(jsonPath("$._embedded.pedidoList.length()").value(2))

                // Validar contenido del primer pedido
                .andExpect(jsonPath("$._embedded.pedidoList[0].cliente").value("Cliente 1"))
                .andExpect(jsonPath("$._embedded.pedidoList[0].producto").value("Producto A"))

                // Validar links HATEOAS del primer pedido
                .andExpect(jsonPath("$._embedded.pedidoList[0]._links.todos-los-pedidos.href").exists())
                .andExpect(jsonPath("$._embedded.pedidoList[0]._links.eliminar.href").exists())

                // Validar contenido del segundo pedido
                .andExpect(jsonPath("$._embedded.pedidoList[1].cliente").value("Cliente 2"))
                .andExpect(jsonPath("$._embedded.pedidoList[1].producto").value("Producto B"))

                // Validar links HATEOAS del segundo pedido
                .andExpect(jsonPath("$._embedded.pedidoList[1]._links.todos-los-pedidos.href").exists())
                .andExpect(jsonPath("$._embedded.pedidoList[1]._links.eliminar.href").exists())

                // Validar self link de toda la colección
                .andExpect(jsonPath("$._links.self.href").exists());
    }
}

