package com.evaluacion2.envios.repository;

import com.evaluacion2.envios.model.Envios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

@Repository
public interface EnviosRepository extends JpaRepository <Envios, Long> {
    List<Envios> findByEstadoEnvio(String estadoEnvio);

    List<Envios> findByFechaEnvio(Date fechaEnvio);

}
