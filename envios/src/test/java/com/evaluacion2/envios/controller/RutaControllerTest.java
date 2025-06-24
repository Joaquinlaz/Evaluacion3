package com.evaluacion2.envios.controller;

import com.evaluacion2.envios.model.Envios;
import com.evaluacion2.envios.model.Ruta;
import com.evaluacion2.envios.service.EnviosService;
import com.evaluacion2.envios.service.RutaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RutaController.class)
public class RutaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RutaService rutaService;

    @MockBean
    private EnviosService enviosService;

    private Ruta ruta;
    private Envios envio;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        envio = new Envios();
        envio.setId(1L);

        ruta = new Ruta();
        ruta.setId(1L);
        ruta.setNombreRuta("Ruta A");
        ruta.setDireccionDestino("Destino");
        ruta.setComunaOrigen("Comuna A");
        ruta.setComunaDestino("Comuna B");
        ruta.setEnvio(envio);
    }

    @Test
    void testCreateRuta() throws Exception {
        when(enviosService.getEnvioById(1L)).thenReturn(envio);
        when(rutaService.saveRuta(any(Ruta.class))).thenReturn(ruta);

        mockMvc.perform(post("/api/v2/rutas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ruta)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetRutaById() throws Exception {
        when(rutaService.getRutaById(1L)).thenReturn(ruta);

        mockMvc.perform(get("/api/v2/rutas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    void testGetRutasByCiudad() throws Exception {
        when(rutaService.findByComunaOrigen("Comuna A")).thenReturn(List.of(ruta));

        mockMvc.perform(get("/api/v2/rutas/comuna/Comuna A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.rutaList[0].id").value(1L));
    }

    @Test
    void testGetAllRutas() throws Exception {
        when(rutaService.getAllRutas()).thenReturn(List.of(ruta));

        mockMvc.perform(get("/api/v2/rutas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.rutaList[0].id").value(1L));
    }

    @Test
    void testUpdateRuta() throws Exception {
        when(rutaService.getRutaById(1L)).thenReturn(ruta);
        when(rutaService.saveRuta(any(Ruta.class))).thenReturn(ruta);

        mockMvc.perform(patch("/api/v2/rutas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ruta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    void testDeleteRuta() throws Exception {
        doNothing().when(rutaService).deleteRuta(1L);

        mockMvc.perform(delete("/api/v2/rutas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testPutRuta() throws Exception {
        when(rutaService.getRutaById(1L)).thenReturn(ruta);
        when(rutaService.saveRuta(any(Ruta.class))).thenReturn(ruta);

        mockMvc.perform(put("/api/v2/rutas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ruta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

}