package com.evaluacion2.envios.service;

import com.evaluacion2.envios.model.Envios;
import com.evaluacion2.envios.repository.RutaRepository;
import com.evaluacion2.envios.model.Ruta;
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
public class RutaServiceTest {
    @MockitoBean
    private RutaRepository rutaRepository;

    @Autowired
    private RutaService rutaService;

    private Ruta fakeRuta;
    private Date now;
    private Envios sampleEnvio;

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
        fakeRuta = new Ruta();
        fakeRuta.setId(1L);
        fakeRuta.setComunaOrigen("Santiago");
        fakeRuta.setComunaDestino("Valparaíso");
        fakeRuta.setNombreRuta("Santiago a Valparaíso");
        fakeRuta.setEnvio(sampleEnvio);
    }

    @Test
    void testSaveRuta() {
        when(rutaRepository.save(fakeRuta)).thenReturn(fakeRuta);

        Ruta saved = rutaService.saveRuta(fakeRuta);

        assertNotNull(saved);
        assertEquals(fakeRuta, saved);
        verify(rutaRepository, times(1)).save(fakeRuta);
    }

    @Test
    void testGetAllRutas() {
        when(rutaRepository.findAll()).thenReturn(List.of(fakeRuta));

        List<Ruta> rutas = rutaService.getAllRutas();

        assertNotNull(rutas);
        assertFalse(rutas.isEmpty());
        assertEquals(1, rutas.size());
        assertEquals(fakeRuta, rutas.get(0));
    }

    @Test
    void testGetRutaById() {
        when(rutaRepository.findById(1L)).thenReturn(Optional.of(fakeRuta));

        Ruta found = rutaService.getRutaById(1L);

        assertNotNull(found);
        assertEquals(fakeRuta.getId(), found.getId());
    }

    @Test
    void testDeleteRuta() {
        doNothing().when(rutaRepository).deleteById(1L);

        rutaService.deleteRuta(1L);

        verify(rutaRepository, times(1)).deleteById(1L);
    }
}
