package com.roomreservas.application.usecase.reserva;

import com.roomreservas.application.dto.request.CriarReservaRequest;
import com.roomreservas.application.dto.response.ReservaResponse;
import com.roomreservas.domain.entity.Reserva;
import com.roomreservas.domain.entity.Sala;
import com.roomreservas.domain.entity.Usuario;
import com.roomreservas.domain.exception.*;
import com.roomreservas.domain.repository.ReservaRepository;
import com.roomreservas.domain.repository.SalaRepository;
import com.roomreservas.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CriarReservaUseCase")
class CriarReservaUseCaseTest {

    @Mock private ReservaRepository reservaRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private SalaRepository salaRepository;

    @InjectMocks
    private CriarReservaUseCase useCase;

    private Usuario usuario;
    private Sala sala;
    private CriarReservaRequest requestValido;
    private OffsetDateTime inicio;
    private OffsetDateTime fim;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .nome("Ana Silva")
                .email("ana@email.com")
                .build();

        sala = Sala.builder()
                .id(UUID.randomUUID())
                .nome("Sala Alpha")
                .capacidade(10)
                .ativa(true)
                .valorDiaria(new BigDecimal("480.00"))
                .build();

        inicio = OffsetDateTime.now().plusDays(2);
        fim = inicio.plusHours(2);

        requestValido = CriarReservaRequest.builder()
                .usuarioId(usuario.getId())
                .salaId(sala.getId())
                .inicio(inicio)
                .fim(fim)
                .build();
    }

    @Test
    @DisplayName("deve criar reserva com sucesso e calcular valor total")
    void deveCriarReservaComSucesso() {
        when(usuarioRepository.buscarPorId(usuario.getId())).thenReturn(Optional.of(usuario));
        when(salaRepository.buscarPorId(sala.getId())).thenReturn(Optional.of(sala));
        when(reservaRepository.contarReservasAtivasPorUsuario(usuario.getId())).thenReturn(0L);
        when(reservaRepository.existeConflitoDeHorario(any(), any(), any(), any())).thenReturn(false);
        when(reservaRepository.salvar(any())).thenAnswer(inv -> {
            Reserva r = inv.getArgument(0);
            r.setId(UUID.randomUUID());
            r.setDataCriacao(OffsetDateTime.now());
            return r;
        });

        ReservaResponse response = useCase.executar(requestValido);

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(Reserva.Status.PENDENTE);
        // 2 horas / 24h * R$480 = R$40
        assertThat(response.valorTotal()).isEqualByComparingTo(new BigDecimal("40.00"));
    }

    @Test
    @DisplayName("deve lançar SalaInativaException quando sala está inativa")
    void deveLancarExcecaoQuandoSalaInativa() {
        sala.setAtiva(false);
        when(usuarioRepository.buscarPorId(any())).thenReturn(Optional.of(usuario));
        when(salaRepository.buscarPorId(any())).thenReturn(Optional.of(sala));

        assertThatThrownBy(() -> useCase.executar(requestValido))
                .isInstanceOf(SalaInativaException.class);
    }

    @Test
    @DisplayName("deve lançar LimiteDeReservasException quando usuário atingiu 3 reservas")
    void deveLancarExcecaoQuandoAtingeLimiteDeReservas() {
        when(usuarioRepository.buscarPorId(any())).thenReturn(Optional.of(usuario));
        when(salaRepository.buscarPorId(any())).thenReturn(Optional.of(sala));
        when(reservaRepository.contarReservasAtivasPorUsuario(any())).thenReturn(3L);

        assertThatThrownBy(() -> useCase.executar(requestValido))
                .isInstanceOf(LimiteDeReservasException.class);
    }

    @Test
    @DisplayName("deve lançar ConflitoDeHorarioException quando há conflito de horário")
    void deveLancarExcecaoQuandoHaConflito() {
        when(usuarioRepository.buscarPorId(any())).thenReturn(Optional.of(usuario));
        when(salaRepository.buscarPorId(any())).thenReturn(Optional.of(sala));
        when(reservaRepository.contarReservasAtivasPorUsuario(any())).thenReturn(0L);
        when(reservaRepository.existeConflitoDeHorario(any(), any(), any(), any())).thenReturn(true);

        assertThatThrownBy(() -> useCase.executar(requestValido))
                .isInstanceOf(ConflitoDeHorarioException.class);
    }

    @Test
    @DisplayName("deve lançar PeriodoInvalidoException quando fim é antes do início")
    void deveLancarExcecaoQuandoPeriodoInvalido() {
        CriarReservaRequest requestInvalido = CriarReservaRequest.builder()
                .usuarioId(usuario.getId())
                .salaId(sala.getId())
                .inicio(fim)
                .fim(inicio) // invertido
                .build();

        assertThatThrownBy(() -> useCase.executar(requestInvalido))
                .isInstanceOf(PeriodoInvalidoException.class);
    }

    @Test
    @DisplayName("deve lançar PeriodoInvalidoException quando duração é menor que 30 minutos")
    void deveLancarExcecaoQuandoDuracaoMenorQue30Min() {
        CriarReservaRequest requestCurto = CriarReservaRequest.builder()
                .usuarioId(usuario.getId())
                .salaId(sala.getId())
                .inicio(inicio)
                .fim(inicio.plusMinutes(20))
                .build();

        assertThatThrownBy(() -> useCase.executar(requestCurto))
                .isInstanceOf(PeriodoInvalidoException.class);
    }

    @Test
    @DisplayName("deve lançar RecursoNaoEncontradoException quando usuário não existe")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.buscarPorId(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(requestValido))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Usuário");
    }
}
