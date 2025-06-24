package com.evaluacion2.Clientes.assemblers;

import com.evaluacion2.Clientes.clientes.controller.ClienteController;
import com.evaluacion2.Clientes.clientes.model.Cliente;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ClienteAssembler implements RepresentationModelAssembler<Cliente, EntityModel<Cliente>> {
    @Override
    public EntityModel<Cliente> toModel(Cliente cliente) {
        return EntityModel.of(cliente,
                linkTo(methodOn(ClienteController.class).getClienteById(cliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteController.class).getAllClientes()).withRel("clientes")
        );
    }
}