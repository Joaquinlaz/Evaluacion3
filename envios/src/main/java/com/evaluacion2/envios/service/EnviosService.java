package com.evaluacion2.envios.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.evaluacion2.envios.repository.EnviosRepository;
import com.evaluacion2.envios.model.Envios;

import java.util.List;
import java.util.Date;

@Service
@Transactional
public class EnviosService {
    @Autowired
    private EnviosRepository enviosRepository;

    public Envios saveEnvio(Envios envio) {
        return enviosRepository.save(envio);
    }

    public Envios getEnvioById(Long id) {
        return enviosRepository.findById(id).orElse(null);
    }

    public List<Envios> getAllEnvios() {
        return enviosRepository.findAll();
    }

    public void deleteEnvio(Long id) {
        enviosRepository.deleteById(id);
    }

    public List<Envios> findByEstado(String estado) {
        return enviosRepository.findByEstadoEnvio(estado);
    }

    public List<Envios> findByFecha(Date fecha) {
        return enviosRepository.findByFechaEnvio(fecha);
    }

}
