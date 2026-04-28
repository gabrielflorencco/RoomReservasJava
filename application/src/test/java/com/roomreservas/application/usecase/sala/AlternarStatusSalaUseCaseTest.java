package com.roomreservas.application.usecase.sala;

import com.roomreservas.application.dto.response.SalaResponse;
import com.roomreservas.domain.entity.Reserva;
import com.roomreservas.domain.entity.Sala;
import com.roomreservas.domain.entity.Usuario;
import com.roomreservas.domain.repository.ReservaRepository;
import com.roomreservas.domain.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlternarStatusSalaUseCase")
class AlternarStatusSalaUseCaseTest {

    @Mock private SalaRepository salaRepository;
    @Mock private ReservaRepository reservaRepository;

    @InjectMocks
    private AlternarStatusSalaUseCase useCase;

    private Sala salaAtiva;

    @BeforeEach
    void setUp() {
        salaAtiva = Sala.builder()
                .id(UUID.randomUUID())
                .nome("Sala Alpha")
                .capacidade(10)
                .ativa(true)
                .valorDiaria(new BigDecimal("500.00"))
                .build();
    }

    @Test
    @DisplayName("deve desativar sala e cancelar reservas futuras")
    void deveDesativarSalaECancelarReservas() {
        Usuario usuario = Usuario.builder().id(UUID.randomUUID()).nome("Ana").email("ana@email.com").build();
        Reserva reservaFutura = Reserva.builder()
                .id(UUID.randomUUID())
                .usuario(usuario)
                .sala(salaAtiva)
                .inicio(OffsetDateTime.now().plusDays(5))
                .fim(OffsetDateTime.now().plusDays(5).plusHours(2))
                .valorTotal(BigDecimal.TEN)
                .status(Reserva.Status.CONFIRMADA)
                .dataCriacao(OffsetDateTime.now())
                .build();

        when(salaRepository.buscarPorId(salaAtiva.getId())).thenReturn(Optional.of(salaAtiva));
        when(reservaRepository.listarReservasFuturasAtivasPorSala(salaAtiva.getId()))
                .thenReturn(List.of(reservaFutura));
        when(salaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        SalaResponse response = useCase.executar(salaAtiva.getId());

        assertThat(response.ativa()).isFalse();
        assertThat(reservaFutura.getStatus()).isEqualTo(Reserva.Status.CANCELADA);
        verify(reservaRepository).salvarTodas(anyList());
    }

    @Test
    @DisplayName("deve reativar sala sem cancelar reservas")
    void deveReativarSala() {
        salaAtiva.setAtiva(false);
        when(salaRepository.buscarPorId(salaAtiva.getId())).thenReturn(Optional.of(salaAtiva));
        when(salaRepository.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

        SalaResponse response = useCase.executar(salaAtiva.getId());

        assertThat(response.ativa()).isTrue();
        verify(reservaRepository, never()).listarReservasFuturasAtivasPorSala(any());
    }
}
