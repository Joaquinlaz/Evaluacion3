package com.evaluacion2.envios.assembler;

import com.evaluacion2.envios.controller.EnviosController;
import com.evaluacion2.envios.model.Envios;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EnviosAssembler implements RepresentationModelAssembler<Envios, EntityModel<Envios>> {

    @Override
    public EntityModel<Envios> toModel(Envios envio) {
        return EntityModel.of(envio,
                linkTo(methodOn(EnviosController.class).getEnvioById(envio.getId())).withSelfRel(),
                linkTo(methodOn(EnviosController.class).getAllEnvios()).withRel("envios")
        );
    }
}