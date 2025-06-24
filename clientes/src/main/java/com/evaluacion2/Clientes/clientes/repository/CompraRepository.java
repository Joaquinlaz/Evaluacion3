package com.evaluacion2.Clientes.clientes.repository;

import com.evaluacion2.Clientes.clientes.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> findByClienteId(Long clienteId);
    List<Compra> findByPrecioTotalBetween(Double minPrice, Double maxPrice);
    List<Compra> findByFechaCompraBetween(Date startDate, Date endDate);

}
