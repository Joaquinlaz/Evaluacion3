package com.evaluacion2.Clientes.clientes.controller;

import com.evaluacion2.Clientes.clientes.model.Soporte;
import com.evaluacion2.Clientes.clientes.model.Cliente;
import com.evaluacion2.Clientes.clientes.service.SoporteService;
import com.evaluacion2.Clientes.clientes.service.ClienteService;

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

@WebMvcTest(SoporteController.class)
public class SoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SoporteService soporteService;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Soporte fakeSoporte;
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

        fakeSoporte = new Soporte();
        fakeSoporte.setId(1L);
        fakeSoporte.setCliente(sampleCliente);
        fakeSoporte.setFechaCreacion(now);
        fakeSoporte.setEstado("Abierto");
        fakeSoporte.setMensaje("Problema con el servicio");
        fakeSoporte.setRespuesta("En proceso de revisión");
    }

    @Test
    public void testGetAllSoportes() throws Exception {
        when(soporteService.getAllSoportes()).thenReturn(List.of(fakeSoporte));

        mockMvc.perform(get("/api/v2/soporte"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.soporteList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.soporteList[0].mensaje").value("Problema con el servicio"))
                .andExpect(jsonPath("$._embedded.soporteList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    public void testGetSoporteById() throws Exception {
        when(soporteService.getSoporteById(1L)).thenReturn(fakeSoporte);

        mockMvc.perform(get("/api/v2/soporte/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.mensaje").value("Problema con el servicio"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.soportes.href").exists());
    }

    @Test
    public void testCreateSoporte() throws Exception {
        when(soporteService.saveSoporte(any(Soporte.class))).thenReturn(fakeSoporte);

        mockMvc.perform(post("/api/v2/soporte")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fakeSoporte)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.mensaje").value("Problema con el servicio"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.soportes.href").exists());
    }

    @Test
    public void testDeleteSoporte() throws Exception {
        doNothing().when(soporteService).deleteSoporte(1L);

        mockMvc.perform(delete("/api/v2/soporte/1"))
                .andExpect(status().isNoContent());

        verify(soporteService, times(1)).deleteSoporte(1L);
    }
}
