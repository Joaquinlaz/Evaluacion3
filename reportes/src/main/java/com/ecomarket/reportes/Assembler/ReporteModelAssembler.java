package com.ecomarket.reportes.Assembler;

import com.ecomarket.reportes.Controller.ReporteController;
import com.ecomarket.reportes.Model.Reporte;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReporteModelAssembler implements RepresentationModelAssembler<Reporte, EntityModel<Reporte>> {

    @Override
    public EntityModel<Reporte> toModel(Reporte reporte) {
        return EntityModel.of(reporte,
                linkTo(methodOn(ReporteController.class).obtenerReporte(reporte.getTipo())).withRel("ver-reporte"),
                linkTo(methodOn(ReporteController.class).listarReportes()).withRel("todos-los-reportes"),
                linkTo(methodOn(ReporteController.class).estadoSistema()).withRel("estado-sistema")
        );
    }
}
