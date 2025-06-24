package com.evaluacion2.Clientes.assemblers;

import com.evaluacion2.Clientes.clientes.controller.SoporteController;
import com.evaluacion2.Clientes.clientes.model.Soporte;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SoporteAssembler implements RepresentationModelAssembler<Soporte, EntityModel<Soporte>> {
    @Override
    public EntityModel<Soporte> toModel(Soporte soporte) {
        return EntityModel.of(soporte,
                linkTo(methodOn(SoporteController.class).getSoporteById(soporte.getId())).withSelfRel(),
                linkTo(methodOn(SoporteController.class).getAllSoportes()).withRel("soportes")
        );
    }
}