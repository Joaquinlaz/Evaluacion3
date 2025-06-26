package com.ecomarket.ecomarket.Service;

import com.ecomarket.ecomarket.Model.Pedido;
import com.ecomarket.ecomarket.Repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PedidoService {
     private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    public List<Pedido> obtenerPedidos() {
        return repository.findAll();
    }

    public Pedido guardarPedido(Pedido pedido) {
        return repository.save(pedido);
    }

    public void eliminarPedido(Long id) {
        repository.deleteById(id);
    }

}
