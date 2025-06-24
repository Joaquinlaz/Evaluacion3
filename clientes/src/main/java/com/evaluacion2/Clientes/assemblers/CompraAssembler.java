package com.evaluacion2.Clientes.assemblers;

import com.evaluacion2.Clientes.clientes.controller.CompraController;
import com.evaluacion2.Clientes.clientes.model.Compra;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CompraAssembler implements RepresentationModelAssembler<Compra, EntityModel<Compra>> {
    @Override
    public EntityModel<Compra> toModel(Compra compra) {
        return EntityModel.of(compra,
                linkTo(methodOn(CompraController.class).getCompraById(compra.getId())).withSelfRel(),
                linkTo(methodOn(CompraController.class).getAllCompras()).withRel("compras")
        );
    }
}