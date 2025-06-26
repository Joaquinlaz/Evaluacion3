package com.ecomarket.reportes.Service;

import com.ecomarket.reportes.Model.Reporte;
import com.ecomarket.reportes.Repository.ReporteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteService {

    private final ReporteRepository repository;

    public ReporteService(ReporteRepository repository) {
        this.repository = repository;
    }

    public Reporte generarReporte(String tipo) {
        String contenido;

        if (tipo.equalsIgnoreCase("ventas")) {
            contenido = "Reporte de ventas generado.";
        } else if (tipo.equalsIgnoreCase("inventario")) {
            contenido = "Reporte de inventario generado.";
        } else if (tipo.equalsIgnoreCase("tiendas")) {
            contenido = "Reporte de tiendas generado.";
        } else {
            contenido = "Tipo de reporte desconocido.";
        }

        Reporte reporte = Reporte.builder()
                .tipo(tipo)
                .contenido(contenido)
                .fecha(LocalDateTime.now())
                .build();

        return repository.save(reporte); 
    }

    public String verificarEstadoSistema() {
        return " Sistema funcionando con normalidad :) ";
    }

    public List<Reporte> obtenerTodosLosReportes() {
    return repository.findAll();
    }
}
