package com.roomreservas.application.usecase.usuario;

import com.roomreservas.application.dto.request.CriarUsuarioRequest;
import com.roomreservas.application.dto.response.UsuarioResponse;
import com.roomreservas.domain.entity.Usuario;
import com.roomreservas.domain.exception.EmailJaCadastradoException;
import com.roomreservas.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse executar(CriarUsuarioRequest request) {
        if (usuarioRepository.existePorEmail(request.email())) {
            throw new EmailJaCadastradoException(request.email());
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome().trim())
                .email(request.email().toLowerCase().trim())
                .build();

        Usuario salvo = usuarioRepository.salvar(usuario);
        return toResponse(salvo);
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail());
    }
}
