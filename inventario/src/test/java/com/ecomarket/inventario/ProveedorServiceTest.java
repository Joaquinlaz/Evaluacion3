package com.ecomarket.inventario;

import com.ecomarket.inventario.model.Proveedor;
import com.ecomarket.inventario.repository.ProveedorRepository;
import com.ecomarket.inventario.service.impl.ProveedorServiceImpl;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorServiceImpl proveedorService;

    private Faker faker;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        faker = new Faker();
    }

    @Test
    void testFindAll() {
        Proveedor proveedor1 = new Proveedor(
                faker.company().name(),
                faker.internet().emailAddress(),
                faker.address().fullAddress(),
                faker.phoneNumber().cellPhone()
        );

        Proveedor proveedor2 = new Proveedor(
                faker.company().name(),
                faker.internet().emailAddress(),
                faker.address().fullAddress(),
                faker.phoneNumber().cellPhone()
        );

        when(proveedorRepository.findAll()).thenReturn(Arrays.asList(proveedor1, proveedor2));

        List<Proveedor> resultado = proveedorService.findAll();

        assertEquals(2, resultado.size());
        verify(proveedorRepository, times(1)).findAll();
    }
}
