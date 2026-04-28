package com.roomreservas.application.usecase.reserva;

import com.roomreservas.application.dto.response.ReservaResponse;
import com.roomreservas.domain.entity.Reserva;
import com.roomreservas.domain.exception.DomainException;
import com.roomreservas.domain.exception.RecursoNaoEncontradoException;
import com.roomreservas.domain.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmarReservaUseCase {

    private final ReservaRepository reservaRepository;

    public ReservaResponse executar(UUID id) {
        Reserva reserva = reservaRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva", id));

        if (reserva.getStatus() != Reserva.Status.PENDENTE) {
            throw new DomainException("Somente reservas com status PENDENTE podem ser confirmadas.") {};
        }

        reserva.setStatus(Reserva.Status.CONFIRMADA);
        Reserva atualizada = reservaRepository.salvar(reserva);
        return ReservaResponseMapper.toResponse(atualizada);
    }
}
