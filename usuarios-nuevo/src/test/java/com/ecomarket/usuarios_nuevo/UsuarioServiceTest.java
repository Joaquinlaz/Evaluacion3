package com.ecomarket.usuarios_nuevo;

import com.ecomarket.usuarios_nuevo.model.Usuario;
import com.ecomarket.usuarios_nuevo.repository.UsuarioRepository;
import com.ecomarket.usuarios_nuevo.service.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario(1L, "Juan", "juan@mail.com", "admin");
    }

    @Test
    void testObtenerTodos() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        assertEquals(1, usuarioService.obtenerTodos().size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void testCrear() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario creado = usuarioService.crear(usuario);
        assertEquals("admin", creado.getRol());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testActualizar() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario actualizado = usuarioService.actualizar(1L, usuario);
        assertNotNull(actualizado);
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testEliminar() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        usuarioService.eliminar(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}
