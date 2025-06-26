package com.ecomarket.reportes.Controller;

import com.ecomarket.reportes.Model.Reporte;
import com.ecomarket.reportes.Service.ReporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/reportes")
public class ReporteController {

    private final ReporteService service;

    public ReporteController(ReporteService service) {
        this.service = service;
    }

    @Operation(
            summary = "Obtener un reporte específico",
            description = "Este endpoint devuelve un reporte basado en el tipo especificado en la ruta. Ejemplos de tipos válidos: `ventas`, `inventario`, `tiendas`."
    )
    @GetMapping("/{tipo}")
    public Reporte obtenerReporte(
            @Parameter(description = "Tipo de reporte a generar. Valores posibles: `ventas`, `inventario`, `tiendas`.") 
            @PathVariable String tipo) {
        return service.generarReporte(tipo);
    }

    @Operation(
            summary = "Verificar el estado del sistema",
            description = "Este endpoint devuelve el estado actual del sistema para confirmar que está operativo."
    )
    @GetMapping("/estado")
    public String estadoSistema() {
        return service.verificarEstadoSistema();
    }

    @Operation(
            summary = "Listar todos los reportes",
            description = "Este endpoint devuelve una lista completa de todos los reportes generados hasta el momento."
    )
    @GetMapping
    public List<Reporte> listarReportes() {
        return service.obtenerTodosLosReportes();
    }
}
