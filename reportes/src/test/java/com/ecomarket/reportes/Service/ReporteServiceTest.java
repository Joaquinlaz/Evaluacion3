
package com.ecomarket.reportes.Service;

import com.ecomarket.reportes.Model.Reporte;
import com.ecomarket.reportes.Repository.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    private ReporteService reporteService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); 
        reporteService = new ReporteService(reporteRepository); 
    }

    @Test
    @DisplayName("Debe generar un reporte de tipo ventas")
    void testGenerarReporteVentas() {
        
        Reporte reporteMock = Reporte.builder()
                .tipo("ventas")
                .contenido("Reporte de ventas generado.")
                .fecha(LocalDateTime.now())
                .build();

        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporteMock);

        Reporte reporte = reporteService.generarReporte("ventas");

       
        assertNotNull(reporte);
        assertEquals("ventas", reporte.getTipo());
        assertEquals("Reporte de ventas generado.", reporte.getContenido());

       
        verify(reporteRepository).save(any(Reporte.class));
    }

    @Test
    @DisplayName("Debe retornar estado OK del sistema")
    void testVerificarEstadoSistema() {
  
        String estado = reporteService.verificarEstadoSistema();

        assertEquals(" Sistema funcionando con normalidad :) ", estado);
    }
}
