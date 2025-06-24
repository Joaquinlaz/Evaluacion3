package com.evaluacion2.envios.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.evaluacion2.envios.model.Ruta;
import com.evaluacion2.envios.repository.RutaRepository;
import java.util.List;

@Service
@Transactional
public class RutaService {
    @Autowired
    private RutaRepository rutaRepository;

    public Ruta saveRuta(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public Ruta getRutaById(Long id) {
        return rutaRepository.findById(id).orElse(null);
    }

    public List<Ruta> getAllRutas() {
        return rutaRepository.findAll();
    }
    public void deleteRuta(Long id) {
        rutaRepository.deleteById(id);
    }

    public List<Ruta> findByComunaOrigen(String origen) {
        return rutaRepository.findByComunaOrigen(origen);
    }


}
