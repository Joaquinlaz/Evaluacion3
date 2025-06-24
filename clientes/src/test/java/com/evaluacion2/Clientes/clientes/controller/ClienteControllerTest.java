package com.evaluacion2.Clientes.clientes.controller;

import com.evaluacion2.Clientes.clientes.model.Cliente;
import com.evaluacion2.Clientes.clientes.service.ClienteService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente sampleCliente;
    private Date now;

    @BeforeEach
    void setUp() {
        now = new Date();
        sampleCliente = new Cliente();
        sampleCliente.setId(1L);
        sampleCliente.setUsername("john_doe");
        sampleCliente.setRun("12345678-9");
        sampleCliente.setCorreo("john@example.com");
        sampleCliente.setCiudad("Santiago");
        sampleCliente.setFechaNacimiento(now);
        sampleCliente.setFechaRegistro(now);
        sampleCliente.setAddress("123 Main St");
    }

    @Test
    public void testCreateCliente() throws Exception {
        when(clienteService.saveCliente(any(Cliente.class))).thenReturn(sampleCliente);

        mockMvc.perform(post("/api/v2/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    public void testGetAllClientes() throws Exception {
        when(clienteService.getAllClientes()).thenReturn(List.of(sampleCliente));

        mockMvc.perform(get("/api/v2/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.clienteList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.clienteList[0].username").value("john_doe"))
                .andExpect(jsonPath("$._embedded.clienteList[0]._links.self.href").exists());
    }

    @Test
    public void testGetClienteById() throws Exception {
        when(clienteService.getClienteById(1L)).thenReturn(sampleCliente);

        mockMvc.perform(get("/api/v2/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    public void testGetClientesByUsername() throws Exception {
        when(clienteService.findByUsername("john_doe")).thenReturn(List.of(sampleCliente));

        mockMvc.perform(get("/api/v2/clientes/username/john_doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.clienteList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.clienteList[0].username").value("john_doe"))
                .andExpect(jsonPath("$._embedded.clienteList[0]._links.self.href").exists());
    }

    @Test
    public void testGetClientesByCiudad() throws Exception {
        when(clienteService.findByCiudad("Santiago")).thenReturn(List.of(sampleCliente));

        mockMvc.perform(get("/api/v2/clientes/ciudad/Santiago"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.clienteList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.clienteList[0].ciudad").value("Santiago"))
                .andExpect(jsonPath("$._embedded.clienteList[0]._links.self.href").exists());
    }

    @Test
    public void testGetClientesByFechaRegistro() throws Exception {
        when(clienteService.findByFechaRegistroBetween(any(Date.class), any(Date.class)))
                .thenReturn(List.of(sampleCliente));

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateStr = sdf.format(now);

        mockMvc.perform(get("/api/v2/clientes/fechaRegistro")
                        .param("startDate", dateStr)
                        .param("endDate", dateStr))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.clienteList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.clienteList[0]._links.self.href").exists());
    }

    @Test
    public void testDeleteCliente() throws Exception {
        doNothing().when(clienteService).deleteCliente(1L);

        mockMvc.perform(delete("/api/v2/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).deleteCliente(1L);
    }

    @Test
    public void testPartialUpdateCliente() throws Exception {
        when(clienteService.getClienteById(1L)).thenReturn(sampleCliente);

        Cliente partialUpdate = new Cliente();
        partialUpdate.setUsername("new_username");
        partialUpdate.setCiudad("Valparaiso");

        Cliente updatedCliente = new Cliente();
        updatedCliente.setId(1L);
        updatedCliente.setUsername("new_username");
        updatedCliente.setCiudad("Valparaiso");
        updatedCliente.setRun(sampleCliente.getRun());
        updatedCliente.setCorreo(sampleCliente.getCorreo());
        updatedCliente.setFechaNacimiento(sampleCliente.getFechaNacimiento());
        updatedCliente.setFechaRegistro(sampleCliente.getFechaRegistro());
        updatedCliente.setAddress(sampleCliente.getAddress());

        when(clienteService.saveCliente(any(Cliente.class))).thenReturn(updatedCliente);

        mockMvc.perform(patch("/api/v2/clientes/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("new_username"))
                .andExpect(jsonPath("$.ciudad").value("Valparaiso"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }
}
