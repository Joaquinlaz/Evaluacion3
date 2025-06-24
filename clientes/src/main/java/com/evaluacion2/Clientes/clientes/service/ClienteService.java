package com.evaluacion2.Clientes.clientes.service;

import com.evaluacion2.Clientes.clientes.model.Cliente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.evaluacion2.Clientes.clientes.repository.ClienteRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente saveCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente getClienteById(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public void deleteCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public List<Cliente> findByUsername(String username) {
        return clienteRepository.findByUsername(username);
    }

    public List<Cliente> findByCiudad(String ciudad) {
        return clienteRepository.findByCiudad(ciudad);
    }

    public List<Cliente> findByFechaRegistroBetween(Date startDate, Date endDate) {
        return clienteRepository.findByFechaRegistroBetween(startDate, endDate);
    }
}
