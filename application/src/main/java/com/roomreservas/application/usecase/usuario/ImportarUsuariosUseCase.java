package com.roomreservas.application.usecase.usuario;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.roomreservas.application.dto.request.CriarUsuarioRequest;
import com.roomreservas.application.dto.response.ImportacaoUsuariosResponse;
import com.roomreservas.application.port.in.UsuarioFileParser;
import com.roomreservas.domain.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportarUsuariosUseCase {

    private final UsuarioFileParser parser;
    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final UsuarioRepository usuarioRepository;

    public ImportacaoUsuariosResponse executar(
            InputStream inputStream
    ) {

        List<CriarUsuarioRequest> usuarios =
                parser.parse(inputStream);

        int inseridos = 0;
        int ignorados = 0;
        int falhas = 0;

        List<String> erros = new ArrayList<>();

        for (CriarUsuarioRequest usuario : usuarios) {

            try {

                boolean existe =
                        usuarioRepository.existePorEmail(usuario.email());

                if (existe) {
                    ignorados++;
                    continue;
                }

                criarUsuarioUseCase.executar(usuario);

                inseridos++;

            } catch (Exception ex) {

                falhas++;

                erros.add(
                    "Usuário %s: %s"
                        .formatted(
                            usuario.email(),
                            ex.getMessage()
                        )
                );
            }
        }

        return new ImportacaoUsuariosResponse(
            inseridos,
            ignorados,
            falhas,
            erros
        );
    }
}