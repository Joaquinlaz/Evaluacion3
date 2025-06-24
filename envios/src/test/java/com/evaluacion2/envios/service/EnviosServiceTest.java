package com.evaluacion2.envios.service;

import com.evaluacion2.envios.repository.EnviosRepository;
import com.evaluacion2.envios.model.Envios;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class EnviosServiceTest {

    @MockitoBean
    private EnviosRepository enviosRepository;

    @Autowired
    private EnviosService enviosService;

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
    void testSaveEnvio() {
        when(enviosRepository.save(sampleEnvio)).thenReturn(sampleEnvio);

        Envios result = enviosService.saveEnvio(sampleEnvio);

        assertNotNull(result);
        assertEquals(sampleEnvio, result);
        verify(enviosRepository, times(1)).save(sampleEnvio);
    }

    @Test
    void testGetAllEnvios() {
        when(enviosRepository.findAll()).thenReturn(List.of(sampleEnvio));

        List<Envios> enviosList = enviosService.getAllEnvios();

        assertNotNull(enviosList);
        assertFalse(enviosList.isEmpty());
        assertEquals(1, enviosList.size());
        assertEquals(sampleEnvio, enviosList.get(0));
    }

    @Test
    void testGetEnvioByIdFound() {
        when(enviosRepository.findById(1L)).thenReturn(Optional.of(sampleEnvio));

        Envios found = enviosService.getEnvioById(1L);

        assertNotNull(found);
        assertEquals(sampleEnvio.getId(), found.getId());
    }

    @Test
    void testDeleteEnvio() {
        doNothing().when(enviosRepository).deleteById(1L);

        enviosService.deleteEnvio(1L);

        verify(enviosRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindEnviosByEstado() {
        when(enviosRepository.findByEstadoEnvio("Enviado")).thenReturn(List.of(sampleEnvio));

        List<Envios> foundEnvios = enviosService.findByEstado("Enviado");

        assertNotNull(foundEnvios);
        assertFalse(foundEnvios.isEmpty());
        assertEquals(1, foundEnvios.size());
        assertEquals(sampleEnvio, foundEnvios.get(0));
        verify(enviosRepository, times(1)).findByEstadoEnvio("Enviado");
    }

    @Test
    void testFindByFechaEnvio() {
        when(enviosRepository.findByFechaEnvio(now)).thenReturn(List.of(sampleEnvio));

        List<Envios> foundEnvios = enviosService.findByFecha(now);

        assertNotNull(foundEnvios);
        assertFalse(foundEnvios.isEmpty());
        assertEquals(1, foundEnvios.size());
        assertEquals(sampleEnvio, foundEnvios.get(0));
        verify(enviosRepository, times(1)).findByFechaEnvio(now);
    }
}
