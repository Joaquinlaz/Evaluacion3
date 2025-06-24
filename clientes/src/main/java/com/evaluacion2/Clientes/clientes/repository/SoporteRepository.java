package com.evaluacion2.Clientes.clientes.repository;

import com.evaluacion2.Clientes.clientes.model.Soporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.evaluacion2.Clientes.clientes.model.Cliente;


import java.util.Date;
import java.util.List;

@Repository
public interface SoporteRepository extends JpaRepository<Soporte, Long> {

    List<Soporte> findByEstado(String estado);

    List<Soporte> findByCliente(Cliente cliente);

    List<Soporte> findByFechaCreacionBetween(Date startDate, Date endDate);

}
