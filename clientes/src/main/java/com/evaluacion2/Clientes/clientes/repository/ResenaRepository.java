package com.evaluacion2.Clientes.clientes.repository;

import com.evaluacion2.Clientes.clientes.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository <Resena, Long> {
    List<Resena> findByClienteId(Long clienteId);

    List<Resena> findByRating(int rating);

    List<Resena> findByFechaCreacionBetween(Date startDate, Date endDate);


}
