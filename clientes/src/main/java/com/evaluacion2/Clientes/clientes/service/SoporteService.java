package com.evaluacion2.Clientes.clientes.service;


import com.evaluacion2.Clientes.clientes.model.Cliente;
import com.evaluacion2.Clientes.clientes.model.Soporte;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.evaluacion2.Clientes.clientes.repository.SoporteRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SoporteService {
    @Autowired
    private SoporteRepository soporteRepository;

    public Soporte saveSoporte(Soporte soporte) {
        return soporteRepository.save(soporte);
    }

    public Soporte getSoporteById(Long id) {
        return soporteRepository.findById(id).orElse(null);
    }

    public List<Soporte> getAllSoportes() {
        return soporteRepository.findAll();
    }

    public void deleteSoporte(Long id) {
        soporteRepository.deleteById(id);
    }

    public List<Soporte> findByEstado(String estado) {
        return soporteRepository.findByEstado(estado);
    }

    public List<Soporte> findByCliente(Cliente cliente) {
        return soporteRepository.findByCliente(cliente);
    }

    public List<Soporte> findByFechaCreacionBetween(Date startDate, Date endDate) {
        return soporteRepository.findByFechaCreacionBetween(startDate, endDate);
    }
}
