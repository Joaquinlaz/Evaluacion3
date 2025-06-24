package com.evaluacion2.envios.controller;

import com.evaluacion2.envios.model.Envios;
import com.evaluacion2.envios.service.EnviosService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnviosController.class)
public class EnviosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnviosService enviosService;

    @Autowired
    private ObjectMapper objectMapper;

    private Envios sampleEnvio;
    private Date now;

    @BeforeEach
    void setUp() {
        now = new Date();
        sampleEnvio = new Envios();
        sampleEnvio.setId(1L);
        sampleEnvio.setAddress("123 Main St");
        sampleEnvio.setCiudad("Santiago");
        sampleEnvio.setFechaEnvio(now);
        sampleEnvio.setEstadoEnvio("Enviado");
        sampleEnvio.setNombre("John Envio");
        sampleEnvio.setClienteId(1L);
    }

    @Test
    void testGetAllEnvios() throws Exception {
        when(enviosService.getAllEnvios()).thenReturn(List.of(sampleEnvio));

        mockMvc.perform(get("/api/v2/envios").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.enviosList[0].id").value(sampleEnvio.getId()))
                .andExpect(jsonPath("$._embedded.enviosList[0].address").value(sampleEnvio.getAddress()))
                .andExpect(jsonPath("$._embedded.enviosList[0].ciudad").value(sampleEnvio.getCiudad()))
                .andExpect(jsonPath("$._embedded.enviosList[0].fechaEnvio").exists())
                .andExpect(jsonPath("$._embedded.enviosList[0].estadoEnvio").value(sampleEnvio.getEstadoEnvio()))
                .andExpect(jsonPath("$._embedded.enviosList[0].nombre").value(sampleEnvio.getNombre()))
                .andExpect(jsonPath("$._embedded.enviosList[0].clienteId").value(sampleEnvio.getClienteId()))
                .andExpect(jsonPath("$._embedded.enviosList[0]._links.self.href").exists());

        verify(enviosService, times(1)).getAllEnvios();
    }

    @Test
    void testSaveEnvio() throws Exception {
        when(enviosService.saveEnvio(any(Envios.class))).thenReturn(sampleEnvio);

        mockMvc.perform(post("/api/v2/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEnvio)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.id").value(sampleEnvio.getId()))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todos-los-envios.href").exists());
    }
}