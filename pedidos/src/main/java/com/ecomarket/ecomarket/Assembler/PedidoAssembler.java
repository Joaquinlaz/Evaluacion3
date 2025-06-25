package com.ecomarket.ecomarket.Assembler;


import com.ecomarket.ecomarket.Controller.PedidoController;
import com.ecomarket.ecomarket.Model.Pedido;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<Pedido, EntityModel<Pedido>> {

    @Override
    public EntityModel<Pedido> toModel(Pedido pedido) {
        return EntityModel.of(pedido,
                linkTo(methodOn(PedidoController.class).obtenerPedidos()).withRel("todos-los-pedidos"),
                linkTo(methodOn(PedidoController.class).eliminarPedido(pedido.getId())).withRel("eliminar"),
                linkTo(methodOn(PedidoController.class).crearPedido(pedido)).withRel("crear-nuevo"));
    }
}
