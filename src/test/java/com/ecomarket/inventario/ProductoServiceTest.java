package com.ecomarket.inventario;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ecomarket.inventario.model.Producto;
import com.ecomarket.inventario.repository.ProductoRepository;
import com.ecomarket.inventario.service.impl.ProductoServiceImpl;

import net.datafaker.Faker;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Faker faker;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        faker = new Faker();
    }

    @Test
    void testFindAll() {
        Producto p1 = new Producto(faker.food().ingredient(), "kg", 100, 500);
        Producto p2 = new Producto(faker.food().ingredient(), "kg", 50, 300);
        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> lista = productoService.findAll();

        assertEquals(2, lista.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Producto producto = new Producto(faker.food().ingredient(), "kg", 10, 400);
        producto.setId(1L);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testSave() {
        Producto nuevo = new Producto(faker.food().ingredient(), "kg", 60, 350);
        when(productoRepository.save(nuevo)).thenReturn(nuevo);

        Producto guardado = productoService.save(nuevo);

        assertNotNull(guardado);
        assertEquals(nuevo.getNombre(), guardado.getNombre());
    }

    @Test
    void testDeleteById() {
        Long id = 10L;
        productoService.deleteById(id);
        verify(productoRepository, times(1)).deleteById(id);
    }
}
