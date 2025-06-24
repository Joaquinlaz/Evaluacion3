package com.evaluacion2.Clientes.clientes.repository;

import com.evaluacion2.Clientes.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    List<Cliente> findByUsername(String username);
    List<Cliente> findByCiudad(String ciudad);
    List<Cliente> findByFechaRegistroBetween(Date startDate, Date endDate);
}
