package com.evaluacion2.Clientes.clientes.service;

import com.evaluacion2.Clientes.clientes.model.Compra;
import com.evaluacion2.Clientes.clientes.repository.CompraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CompraService {
    @Autowired
    private CompraRepository compraRepository;

    public Compra saveCompra(Compra compra) {
        return compraRepository.save(compra);
    }

    public Compra getCompraById(Long id) {
        return compraRepository.findById(id).orElse(null);
    }

    public List<Compra> getAllCompras() {
        return compraRepository.findAll();
    }

    public void deleteCompra(Long id) {
        compraRepository.deleteById(id);
    }

    public List<Compra> findByClienteId(Long clienteId) {
        return compraRepository.findByClienteId(clienteId);
    }

    public List<Compra> findByPrecioTotalBetween(Double minPrice, Double maxPrice) {
        return compraRepository.findByPrecioTotalBetween(minPrice, maxPrice);
    }

    public List<Compra> findByFechaCompraBetween(Date startDate, Date endDate) {
        return compraRepository.findByFechaCompraBetween(startDate, endDate);
    }

}
