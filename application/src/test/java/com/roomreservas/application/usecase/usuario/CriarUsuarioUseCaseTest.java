package com.roomreservas.application.usecase.usuario;

import com.roomreservas.application.dto.request.CriarUsuarioRequest;
import com.roomreservas.application.dto.response.UsuarioResponse;
import com.roomreservas.domain.entity.Usuario;
import com.roomreservas.domain.exception.EmailJaCadastradoException;
import com.roomreservas.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CriarUsuarioUseCase")
class CriarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CriarUsuarioUseCase useCase;

    private CriarUsuarioRequest requestValido;

    @BeforeEach
    void setUp() {
        requestValido = new CriarUsuarioRequest("Ana Silva", "ana@email.com");
    }

    @Test
    @DisplayName("deve criar usuário com sucesso quando dados são válidos")
    void deveCriarUsuarioComSucesso() {
        when(usuarioRepository.existePorEmail(anyString())).thenReturn(false);
        when(usuarioRepository.salvar(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });

        UsuarioResponse response = useCase.executar(requestValido);

        assertThat(response).isNotNull();
        assertThat(response.nome()).isEqualTo("Ana Silva");
        assertThat(response.email()).isEqualTo("ana@email.com");
        assertThat(response.id()).isNotNull();
        verify(usuarioRepository).salvar(any(Usuario.class));
    }

    @Test
    @DisplayName("deve normalizar email para minúsculas")
    void deveNormalizarEmail() {
        CriarUsuarioRequest request = new CriarUsuarioRequest("Ana", "ANA@EMAIL.COM");
        when(usuarioRepository.existePorEmail("ana@email.com")).thenReturn(false);
        when(usuarioRepository.salvar(any())).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });

        UsuarioResponse response = useCase.executar(request);

        assertThat(response.email()).isEqualTo("ana@email.com");
    }

    @Test
    @DisplayName("deve lançar EmailJaCadastradoException quando email já existe")
    void deveLancarExcecaoQuandoEmailJaExiste() {
        when(usuarioRepository.existePorEmail("ana@email.com")).thenReturn(true);

        assertThatThrownBy(() -> useCase.executar(requestValido))
                .isInstanceOf(EmailJaCadastradoException.class)
                .hasMessageContaining("ana@email.com");

        verify(usuarioRepository, never()).salvar(any());
    }
}
