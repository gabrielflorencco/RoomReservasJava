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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CriarReservaUseCase {

    private static final int LIMITE_RESERVAS_ATIVAS = 3;

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;

    public ReservaResponse executar(CriarReservaRequest request) {
        // 1. Validar período
        validarPeriodo(request.inicio(), request.fim());

        // 2. Buscar entidades
        Usuario usuario = usuarioRepository.buscarPorId(request.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário", request.usuarioId()));

        Sala sala = salaRepository.buscarPorId(request.salaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sala", request.salaId()));

        // 3. Sala deve estar ativa
        if (!sala.isAtiva()) {
            throw new SalaInativaException();
        }

        // 4. Limite de reservas por usuário
        long reservasAtivas = reservaRepository.contarReservasAtivasPorUsuario(usuario.getId());
        if (reservasAtivas >= LIMITE_RESERVAS_ATIVAS) {
            throw new LimiteDeReservasException();
        }

        // 5. Conflito de horário
        boolean temConflito = reservaRepository.existeConflitoDeHorario(
                sala.getId(), request.inicio(), request.fim(), null);
        if (temConflito) {
            throw new ConflitoDeHorarioException();
        }

        // 6. Calcular valor total (proporção em horas × valor_diaria / 24)
        BigDecimal valorTotal = calcularValorTotal(sala.getValorDiaria(), request.inicio(), request.fim());

        // 7. Criar reserva
        Reserva reserva = Reserva.builder()
                .usuario(usuario)
                .sala(sala)
                .inicio(request.inicio())
                .fim(request.fim())
                .valorTotal(valorTotal)
                .status(Reserva.Status.PENDENTE)
                .build();

        Reserva salva = reservaRepository.salvar(reserva);
        return ReservaResponseMapper.toResponse(salva);
    }

    private void validarPeriodo(OffsetDateTime inicio, OffsetDateTime fim) {
        if (!fim.isAfter(inicio)) {
            throw new PeriodoInvalidoException("A data/hora de fim deve ser posterior ao início.");
        }
        if (Duration.between(inicio, fim).toMinutes() < 30) {
            throw new PeriodoInvalidoException("A reserva deve ter duração mínima de 30 minutos.");
        }
    }

    /**
     * Calcula o valor total baseado no valor da diária (24h).
     * Proporção: horas_reservadas / 24 * valor_diaria
     */
    private BigDecimal calcularValorTotal(BigDecimal valorDiaria, OffsetDateTime inicio, OffsetDateTime fim) {
        long minutos = Duration.between(inicio, fim).toMinutes();
        BigDecimal horas = BigDecimal.valueOf(minutos).divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);
        return valorDiaria.multiply(horas)
                .divide(BigDecimal.valueOf(24), 2, RoundingMode.HALF_UP);
    }
}
