package com.roomreservas.application.usecase.reserva;

import com.roomreservas.application.dto.response.ReservaResponse;
import com.roomreservas.domain.entity.Reserva;
import com.roomreservas.domain.exception.CancelamentoForaDoPrazoException;
import com.roomreservas.domain.exception.ReservaJaCanceladaException;
import com.roomreservas.domain.exception.RecursoNaoEncontradoException;
import com.roomreservas.domain.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelarReservaUseCase {

    private final ReservaRepository reservaRepository;

    public ReservaResponse executar(UUID id) {
        Reserva reserva = reservaRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva", id));

        if (reserva.getStatus() == Reserva.Status.CANCELADA) {
            throw new ReservaJaCanceladaException();
        }

        // Regra: cancelamento só com 24h de antecedência
        OffsetDateTime limite = OffsetDateTime.now().plusHours(24);
        if (reserva.getInicio().isBefore(limite)) {
            throw new CancelamentoForaDoPrazoException();
        }

        reserva.setStatus(Reserva.Status.CANCELADA);
        Reserva atualizada = reservaRepository.salvar(reserva);
        return ReservaResponseMapper.toResponse(atualizada);
    }
}
