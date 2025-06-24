package com.evaluacion2.Clientes.clientes.service;

import com.evaluacion2.Clientes.clientes.model.Resena;
import com.evaluacion2.Clientes.clientes.repository.ResenaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
@Transactional

public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    public Resena saveResena(Resena resena) {
        return resenaRepository.save(resena);
    }

    public Resena getResenaById(Long id) {
        return resenaRepository.findById(id).orElse(null);
    }

    public Resena getResenaByRating(int rating) {
        List<Resena> resenas = resenaRepository.findByRating(rating);
        return resenas.isEmpty() ? null : resenas.getFirst();
    }

    public List<Resena> getAllResenas() {
        return resenaRepository.findAll();
    }

    public void deleteResena(Long id) {
        resenaRepository.deleteById(id);
    }

}
