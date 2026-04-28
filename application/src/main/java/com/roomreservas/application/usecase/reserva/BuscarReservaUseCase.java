package com.roomreservas.application.usecase.reserva;

import com.roomreservas.application.dto.response.ReservaResponse;
import com.roomreservas.domain.entity.Reserva;
import com.roomreservas.domain.exception.RecursoNaoEncontradoException;
import com.roomreservas.domain.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuscarReservaUseCase {

    private final ReservaRepository reservaRepository;

    public ReservaResponse buscarPorId(UUID id) {
        Reserva reserva = reservaRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva", id));
        return ReservaResponseMapper.toResponse(reserva);
    }

    public List<ReservaResponse> listarTodas() {
        return reservaRepository.listarTodas().stream()
                .map(ReservaResponseMapper::toResponse).toList();
    }

    public List<ReservaResponse> listarPorUsuario(UUID usuarioId) {
        return reservaRepository.listarPorUsuario(usuarioId).stream()
                .map(ReservaResponseMapper::toResponse).toList();
    }

    public List<ReservaResponse> listarPorSala(UUID salaId) {
        return reservaRepository.listarPorSala(salaId).stream()
                .map(ReservaResponseMapper::toResponse).toList();
    }
}
