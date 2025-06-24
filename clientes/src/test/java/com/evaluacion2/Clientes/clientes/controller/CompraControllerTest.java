package com.evaluacion2.Clientes.clientes.controller;

import com.evaluacion2.Clientes.clientes.model.Cliente;
import com.evaluacion2.Clientes.clientes.model.Compra;
import com.evaluacion2.Clientes.clientes.service.CompraService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompraController.class)
public class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompraService compraService;

    @Autowired
    private ObjectMapper objectMapper;

    private Compra compra;
    private Cliente cliente;
    private Date now;

    @BeforeEach
    void setUp() {
        now = new Date();

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setUsername("john_doe");
        cliente.setRun("12345678-9");
        cliente.setCorreo("john@example.com");
        cliente.setCiudad("Santiago");
        cliente.setFechaNacimiento(now);
        cliente.setFechaRegistro(now);
        cliente.setAddress("123 Main St");

        compra = new Compra();
        compra.setId(1L);
        compra.setFechaCompra(now);
        compra.setPrecioTotal(100.0);
        compra.setCliente(cliente);
    }

    @Test
    public void testGetAllCompras() throws Exception {
        when(compraService.getAllCompras()).thenReturn(List.of(compra));

        mockMvc.perform(get("/api/v2/compras"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.compraList[0].id").value(1))
                .andExpect(jsonPath("_embedded.compraList[0]._links.self.href").exists());
    }

    @Test
    public void testGetCompraById() throws Exception {
        when(compraService.getCompraById(1L)).thenReturn(compra);

        mockMvc.perform(get("/api/v2/compras/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.compras.href").exists());
    }

    @Test
    public void testCreateCompra() throws Exception {
        when(compraService.saveCompra(any(Compra.class))).thenReturn(compra);

        mockMvc.perform(post("/api/v2/compras/{clienteId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(compra)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.compras.href").exists());
    }

    @Test
    public void testDeleteCompra() throws Exception {
        when(compraService.getCompraById(1L)).thenReturn(compra);
        doNothing().when(compraService).deleteCompra(1L);

        mockMvc.perform(delete("/api/v2/compras/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindByCliente() throws Exception {
        when(compraService.findByClienteId(1L)).thenReturn(List.of(compra));

        mockMvc.perform(get("/api/v2/compras/cliente/{clienteId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.compraList[0].id").value(1))
                .andExpect(jsonPath("_embedded.compraList[0]._links.self.href").exists());
    }
}

