package com.evaluacion2.Clientes.assemblers;

import com.evaluacion2.Clientes.clientes.controller.ResenaController;
import com.evaluacion2.Clientes.clientes.model.Resena;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ResenaAssembler implements RepresentationModelAssembler<Resena, EntityModel<Resena>> {
    @Override
    public EntityModel<Resena> toModel(Resena resena) {
        return EntityModel.of(resena,
                linkTo(methodOn(ResenaController.class).getResenaById(resena.getId())).withSelfRel(),
                linkTo(methodOn(ResenaController.class).getAllResenas()).withRel("resenas")
        );
    }
}