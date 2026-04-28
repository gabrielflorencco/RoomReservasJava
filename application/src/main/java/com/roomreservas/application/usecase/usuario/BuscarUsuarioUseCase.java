package com.roomreservas.application.usecase.usuario;

import com.roomreservas.application.dto.response.UsuarioResponse;
import com.roomreservas.domain.entity.Usuario;
import com.roomreservas.domain.exception.RecursoNaoEncontradoException;
import com.roomreservas.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuscarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse buscarPorId(UUID id) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
        return toResponse(usuario);
    }

    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.listarTodos().stream()
                .map(this::toResponse)
                .toList();
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail());
    }
}
