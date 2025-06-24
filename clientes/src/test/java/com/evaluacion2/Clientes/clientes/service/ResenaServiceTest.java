package com.evaluacion2.Clientes.clientes.service;

import com.evaluacion2.Clientes.clientes.model.Cliente;
import com.evaluacion2.Clientes.clientes.model.Resena;
import com.evaluacion2.Clientes.clientes.repository.ResenaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ResenaServiceTest {

    @MockitoBean
    private ResenaRepository resenaRepository;

    @Autowired
    private ResenaService resenaService;

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
    void testSaveResena() {
        when(resenaRepository.save(sampleResena)).thenReturn(sampleResena);

        Resena result = resenaService.saveResena(sampleResena);

        assertNotNull(result);
        assertEquals(sampleResena, result);
        verify(resenaRepository, times(1)).save(sampleResena);
    }

    @Test
    void testGetResenabyRating(){
        when(resenaRepository.findByRating(5)).thenReturn(Collections.singletonList(sampleResena));

        Resena result = resenaService.getResenaByRating(5);

        assertNotNull(result);
        assertEquals(sampleResena.getId(), result.getId());
        verify(resenaRepository, times(1)).findByRating(5);
    }

    @Test
    void testGetResenaById() {
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(sampleResena));

        Resena result = resenaService.getResenaById(1L);

        assertNotNull(result);
        assertEquals(sampleResena.getId(), result.getId());
        verify(resenaRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllResenas() {
        when(resenaRepository.findAll()).thenReturn(Collections.singletonList(sampleResena));

        List<Resena> resenas = resenaService.getAllResenas();

        assertNotNull(resenas);
        assertEquals(1, resenas.size());
        verify(resenaRepository, times(1)).findAll();
    }

    @Test
    void testDeleteResena() {
        doNothing().when(resenaRepository).deleteById(1L);

        resenaService.deleteResena(1L);

        verify(resenaRepository, times(1)).deleteById(1L);
    }
}
