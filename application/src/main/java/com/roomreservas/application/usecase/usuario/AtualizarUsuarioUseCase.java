package com.roomreservas.application.usecase.usuario;

import com.roomreservas.application.dto.request.AtualizarUsuarioRequest;
import com.roomreservas.application.dto.response.UsuarioResponse;
import com.roomreservas.domain.entity.Usuario;
import com.roomreservas.domain.exception.EmailJaCadastradoException;
import com.roomreservas.domain.exception.RecursoNaoEncontradoException;
import com.roomreservas.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtualizarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse executar(UUID id, AtualizarUsuarioRequest request) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", id));

        String novoEmail = request.email().toLowerCase().trim();
        if (!usuario.getEmail().equals(novoEmail) && usuarioRepository.existePorEmail(novoEmail)) {
            throw new EmailJaCadastradoException(novoEmail);
        }

        usuario.setNome(request.nome().trim());
        usuario.setEmail(novoEmail);

        Usuario atualizado = usuarioRepository.salvar(usuario);
        return new UsuarioResponse(atualizado.getId(), atualizado.getNome(), atualizado.getEmail());
    }
}
