package com.evaluacion2.envios.assembler;

import com.evaluacion2.envios.controller.RutaController;
import com.evaluacion2.envios.model.Ruta;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RutaAssembler implements RepresentationModelAssembler<Ruta, EntityModel<Ruta>> {

    @Override
    public EntityModel<Ruta> toModel(Ruta ruta) {
        return EntityModel.of(ruta,
                linkTo(methodOn(RutaController.class).getRutaById(ruta.getId())).withSelfRel(),
                linkTo(methodOn(RutaController.class).getAllRutas()).withRel("rutas")
        );
    }
}