package com.evaluacion2.Clientes.clientes.controller;

import com.evaluacion2.Clientes.clientes.model.Resena;
import com.evaluacion2.Clientes.clientes.model.Cliente;
import com.evaluacion2.Clientes.clientes.service.ResenaService;
import com.evaluacion2.Clientes.clientes.service.ClienteService;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import java.util.Date;
import java.util.List;

@WebMvcTest(ResenaController.class)
public class ResenaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResenaService resenaService;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Resena sampleResena;
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

        sampleResena = new Resena();
        sampleResena.setId(1L);
        sampleResena.setCliente(sampleCliente);
        sampleResena.setRating(5);
        sampleResena.setComentario("Excelente servicio");
        sampleResena.setFechaCreacion(now);
    }

    @Test
    public void testGetAllResenas() throws Exception {
        when(resenaService.getAllResenas()).thenReturn(List.of(sampleResena));

        mockMvc.perform(get("/api/v2/resenas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.resenaList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.resenaList[0].comentario").value("Excelente servicio"))
                .andExpect(jsonPath("$._embedded.resenaList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    public void testGetResenaById() throws Exception {
        when(resenaService.getResenaById(1L)).thenReturn(sampleResena);

        mockMvc.perform(get("/api/v2/resenas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.comentario").value("Excelente servicio"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.resenas.href").exists());
    }

    @Test
    public void testCreateResena() throws Exception {
        when(clienteService.getClienteById(1L)).thenReturn(sampleCliente);
        when(resenaService.saveResena(any(Resena.class))).thenReturn(sampleResena);

        mockMvc.perform(post("/api/v2/resenas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleResena)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.comentario").value("Excelente servicio"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.resenas.href").exists());
    }

    @Test
    public void testGetResenaByRating() throws Exception {
        when(resenaService.getResenaByRating(5)).thenReturn(sampleResena);

        mockMvc.perform(get("/api/v2/resenas/rating/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.resenas.href").exists());
    }
}

