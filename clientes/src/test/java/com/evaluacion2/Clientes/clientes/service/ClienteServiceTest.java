package com.evaluacion2.Clientes.clientes.service;

import com.evaluacion2.Clientes.clientes.model.Cliente;
import com.evaluacion2.Clientes.clientes.repository.ClienteRepository;
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

/**
 * Integration-style test using Spring Boot context with a mocked repository.
 */
@SpringBootTest
public class ClienteServiceTest {

    @MockitoBean
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

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
    void testSaveCliente() {
        when(clienteRepository.save(sampleCliente)).thenReturn(sampleCliente);

        Cliente result = clienteService.saveCliente(sampleCliente);

        assertNotNull(result);
        assertEquals(sampleCliente, result);
        verify(clienteRepository, times(1)).save(sampleCliente);
    }

    @Test
    void testGetClienteByIdFound() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(sampleCliente));

        Cliente result = clienteService.getClienteById(1L);

        assertNotNull(result);
        assertEquals(sampleCliente.getId(), result.getId());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteCliente() {
        doNothing().when(clienteRepository).deleteById(1L);

        clienteService.deleteCliente(1L);

        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(sampleCliente));

        List<Cliente> result = clienteService.getAllClientes();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(sampleCliente, result.get(0));
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testFindByUsername() {
        when(clienteRepository.findByUsername("john_doe")).thenReturn(List.of(sampleCliente));

        List<Cliente> result = clienteService.findByUsername("john_doe");

        assertFalse(result.isEmpty());
        assertEquals(sampleCliente, result.getFirst());
    }

    @Test
    void testFindByCiudad() {
        when(clienteRepository.findByCiudad("Santiago")).thenReturn(List.of(sampleCliente));

        List<Cliente> result = clienteService.findByCiudad("Santiago");

        assertFalse(result.isEmpty());
        assertEquals(sampleCliente, result.getFirst());
        verify(clienteRepository, times(1)).findByCiudad("Santiago");
    }

    @Test
    void testFindByFechaRegistroBetween() {
        Date start = new Date(now.getTime() - 1000000);
        Date end = new Date(now.getTime() + 1000000);
        when(clienteRepository.findByFechaRegistroBetween(start, end)).thenReturn(List.of(sampleCliente));

        List<Cliente> result = clienteService.findByFechaRegistroBetween(start, end);

        assertFalse(result.isEmpty());
        assertEquals(sampleCliente, result.getFirst());
        verify(clienteRepository, times(1)).findByFechaRegistroBetween(start, end);
    }
}

