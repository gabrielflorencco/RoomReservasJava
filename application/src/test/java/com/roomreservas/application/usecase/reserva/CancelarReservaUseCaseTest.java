package com.roomreservas.application.usecase.reserva;

import com.roomreservas.application.dto.response.ReservaResponse;
import com.roomreservas.domain.entity.Reserva;
import com.roomreservas.domain.entity.Sala;
import com.roomreservas.domain.entity.Usuario;
import com.roomreservas.domain.exception.CancelamentoForaDoPrazoException;
import com.roomreservas.domain.exception.ReservaJaCanceladaException;
import com.roomreservas.domain.repository.ReservaRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CancelarReservaUseCase")
class CancelarReservaUseCaseTest {

    @Mock private ReservaRepository reservaRepository;

    @InjectMocks
    private CancelarReservaUseCase useCase;

    private Reserva reserva;

    @BeforeEach
    void setUp() {
        Usuario usuario = Usuario.builder().id(UUID.randomUUID()).nome("Ana").email("ana@email.com").build();
        Sala sala = Sala.builder().id(UUID.randomUUID()).nome("Sala A").capacidade(10).ativa(true).valorDiaria(BigDecimal.TEN).build();

        reserva = Reserva.builder()
                .id(UUID.randomUUID())
                .usuario(usuario)
                .sala(sala)
                .inicio(OffsetDateTime.now().plusDays(3))
                .fim(OffsetDateTime.now().plusDays(3).plusHours(2))
                .valorTotal(BigDecimal.TEN)
                .status(Reserva.Status.PENDENTE)
                .dataCriacao(OffsetDateTime.now())
                .build();
    }

    @Test
    @DisplayName("deve cancelar reserva com sucesso quando dentro do prazo")
    void deveCancelarReservaComSucesso() {
        when(reservaRepository.buscarPorId(reserva.getId())).thenReturn(Optional.of(reserva));
        when(reservaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        ReservaResponse response = useCase.executar(reserva.getId());

        assertThat(response.status()).isEqualTo(Reserva.Status.CANCELADA);
    }

    @Test
    @DisplayName("deve lançar ReservaJaCanceladaException quando reserva já está cancelada")
    void deveLancarExcecaoQuandoJaCancelada() {
        reserva.setStatus(Reserva.Status.CANCELADA);
        when(reservaRepository.buscarPorId(any())).thenReturn(Optional.of(reserva));

        assertThatThrownBy(() -> useCase.executar(reserva.getId()))
                .isInstanceOf(ReservaJaCanceladaException.class);
    }

    @Test
    @DisplayName("deve lançar CancelamentoForaDoPrazoException quando faltam menos de 24h")
    void deveLancarExcecaoQuandoForaDoPrazo() {
        reserva.setInicio(OffsetDateTime.now().plusHours(10)); // menos de 24h
        when(reservaRepository.buscarPorId(any())).thenReturn(Optional.of(reserva));

        assertThatThrownBy(() -> useCase.executar(reserva.getId()))
                .isInstanceOf(CancelamentoForaDoPrazoException.class);
    }
}
