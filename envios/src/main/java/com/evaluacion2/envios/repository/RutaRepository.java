package com.evaluacion2.envios.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.evaluacion2.envios.model.Ruta;
import java.util.List;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {

    List<Ruta> findByEnvioId(Long envioId);

    List<Ruta> findByComunaOrigen(String comunaOrigen);
}
