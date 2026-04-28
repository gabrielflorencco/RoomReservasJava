package com.roomreservas.application.usecase.usuario;

import com.roomreservas.domain.exception.RecursoNaoEncontradoException;
import com.roomreservas.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeletarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public void executar(UUID id) {
        usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));
        usuarioRepository.deletar(id);
    }
}
