package com.evaluacion2.Clientes.clientes.service;

import com.evaluacion2.Clientes.clientes.model.Soporte;
import com.evaluacion2.Clientes.clientes.model.Cliente;
import com.evaluacion2.Clientes.clientes.repository.SoporteRepository;
import com.evaluacion2.Clientes.clientes.repository.CompraRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SoporteServiceTest {

    @MockitoBean
    private SoporteRepository soporteRepository;

    @Autowired
    private SoporteService soporteService;

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
        fakeSoporte.setMensaje("Problema con el producto");
        fakeSoporte.setRespuesta("En proceso de revisión");
    }

    @Test
    void testSaveSoporte() {
        when(soporteRepository.save(fakeSoporte)).thenReturn(fakeSoporte);

        Soporte saved = soporteService.saveSoporte(fakeSoporte);

        assertNotNull(saved);
        assertEquals(fakeSoporte.getId(), saved.getId());
        verify(soporteRepository, times(1)).save(fakeSoporte);
    }

    @Test
    void testGetSoporteById(){
        when(soporteRepository.findById(1L)).thenReturn(Optional.of(fakeSoporte));

        Soporte found = soporteService.getSoporteById(1L);

        assertNotNull(found);
        assertEquals(fakeSoporte.getId(), found.getId());
        verify(soporteRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllSoportes() {
        when(soporteRepository.findAll()).thenReturn(Collections.singletonList(fakeSoporte));

        List<Soporte> soportes = soporteService.getAllSoportes();

        assertNotNull(soportes);
        assertEquals(1, soportes.size());
        assertEquals(fakeSoporte.getId(), soportes.get(0).getId());
        verify(soporteRepository, times(1)).findAll();
    }

    @Test
    void testDeleteSoporte() {
        doNothing().when(soporteRepository).deleteById(1L);

        soporteService.deleteSoporte(1L);

        verify(soporteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByEstado() {
        when(soporteRepository.findByEstado("Abierto")).thenReturn(Collections.singletonList(fakeSoporte));

        List<Soporte> result = soporteService.findByEstado("Abierto");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(fakeSoporte.getId(), result.get(0).getId());
        verify(soporteRepository, times(1)).findByEstado("Abierto");
    }

    @Test
    void testFindByCliente() {
        when(soporteRepository.findByCliente(sampleCliente)).thenReturn(Collections.singletonList(fakeSoporte));

        List<Soporte> result = soporteService.findByCliente(sampleCliente);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(fakeSoporte.getId(), result.get(0).getId());
        verify(soporteRepository, times(1)).findByCliente(sampleCliente);
    }

    @Test
    void testFindByFechaCreacionBetween() {
        Date startDate = new Date(now.getTime() - 10000);
        Date endDate = new Date(now.getTime() + 10000);
        when(soporteRepository.findByFechaCreacionBetween(startDate, endDate)).thenReturn(Collections.singletonList(fakeSoporte));

        List<Soporte> result = soporteService.findByFechaCreacionBetween(startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(fakeSoporte.getId(), result.get(0).getId());
        verify(soporteRepository, times(1)).findByFechaCreacionBetween(startDate, endDate);
    }
}
