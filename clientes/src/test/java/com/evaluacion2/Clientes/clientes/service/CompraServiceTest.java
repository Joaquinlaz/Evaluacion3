package com.evaluacion2.Clientes.clientes.service;

import com.evaluacion2.Clientes.clientes.model.Compra;
import com.evaluacion2.Clientes.clientes.model.Cliente;
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
public class CompraServiceTest {

     @MockitoBean
     private CompraRepository compraRepository;

     @Autowired
     private CompraService compraService;

     private Compra fakeCompra;
     private Cliente sampleCliente;
     private Date now;

     @BeforeEach
     void setUp(){
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
          fakeCompra = new Compra();
          fakeCompra.setId(1L);
          fakeCompra.setCliente(sampleCliente);
          fakeCompra.setFechaCompra(now);
          fakeCompra.setPrecioTotal(10000.0);

     }

     @Test
     void testSaveCompra() {
          when(compraRepository.save(fakeCompra)).thenReturn(fakeCompra);

          Compra saved = compraService.saveCompra(fakeCompra);

          assertNotNull(saved);
          assertEquals(1L, saved.getId());
          assertEquals(10000, saved.getPrecioTotal());
          verify(compraRepository, times(1)).save(fakeCompra);
     }

     @Test
     void testGetCompraById(){
          when(compraRepository.findById(1L)).thenReturn(Optional.of(fakeCompra));

          Compra result = compraService.getCompraById(1L);

          assertNotNull(result);
          assertEquals(fakeCompra.getId(), result.getId());
          verify(compraRepository, times(1)).findById(1L);
     }

     @Test
     void testGetAllCompras(){
          when(compraRepository.findAll()).thenReturn(List.of(fakeCompra));

          List<Compra> result = compraService.getAllCompras();

          assertNotNull(result);
          assertEquals(1, result.size());
          assertEquals(fakeCompra, result.getFirst());

     }

     @Test
     void testDeleteCompra(){
            doNothing().when(compraRepository).deleteById(1L);
            compraService.deleteCompra(1L);
            verify(compraRepository, times(1)).deleteById(1L);
     }

     @Test
     void testFindByClienteId() {
          List<Compra> compras = Collections.singletonList(fakeCompra);
          when(compraRepository.findByClienteId(1L)).thenReturn(compras);

          List<Compra> result = compraService.findByClienteId(1L);

          assertNotNull(result);
          assertEquals(1, result.size());
          assertEquals(1L, result.getFirst().getCliente().getId());
     }

     @Test
     void testFindByPrecioTotalBetween() {
          when(compraRepository.findByPrecioTotalBetween(5000.0, 15000.0))
                  .thenReturn(List.of(fakeCompra));

          List<Compra> result = compraService.findByPrecioTotalBetween(5000.0, 15000.0);

          assertNotNull(result);
          assertEquals(1, result.size());
          assertEquals(10000, result.getFirst().getPrecioTotal());
     }

     @Test
     void testFindByFechaCompraBetween() {
          Date startDate = new Date(now.getTime() - 100000);
          Date endDate = new Date(now.getTime() + 100000);
          when(compraRepository.findByFechaCompraBetween(startDate, endDate))
                  .thenReturn(List.of(fakeCompra));

          List<Compra> result = compraService.findByFechaCompraBetween(startDate, endDate);

          assertNotNull(result);
          assertEquals(1, result.size());
          assertEquals(fakeCompra, result.getFirst());
     }
}
