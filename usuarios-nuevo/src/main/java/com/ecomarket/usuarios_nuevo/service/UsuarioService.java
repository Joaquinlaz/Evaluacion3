package com.ecomarket.usuarios_nuevo.service;

import com.ecomarket.usuarios_nuevo.model.Usuario;

import java.util.List;

public interface UsuarioService {
    List<Usuario> obtenerTodos();
    Usuario obtenerPorId(Long id);
    Usuario crear(Usuario usuario);
    Usuario actualizar(Long id, Usuario usuario);
    void eliminar(Long id);
}