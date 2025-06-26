package com.ecomarket.usuarios_nuevo.repository;

import com.ecomarket.usuarios_nuevo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Puedes añadir métodos de búsqueda personalizados aquí, por ejemplo:
    // Optional<Usuario> findByEmail(String email);
    // List<Usuario> findByRol(String rol);
}