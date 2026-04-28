package com.roomreservas.application.usecase.sala;

import com.roomreservas.application.dto.response.SalaResponse;
import com.roomreservas.domain.entity.Reserva;
import com.roomreservas.domain.entity.Sala;
import com.roomreservas.domain.exception.RecursoNaoEncontradoException;
import com.roomreservas.domain.repository.ReservaRepository;
import com.roomreservas.domain.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlternarStatusSalaUseCase {

    private final SalaRepository salaRepository;
    private final ReservaRepository reservaRepository;

    public SalaResponse executar(UUID id) {
        Sala sala = salaRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sala", id));

        boolean novoStatus = !sala.isAtiva();
        sala.setAtiva(novoStatus);

        // Se desativando: cancelar automaticamente todas as reservas futuras ativas
        if (!novoStatus) {
            List<Reserva> reservasFuturas = reservaRepository.listarReservasFuturasAtivasPorSala(id);
            reservasFuturas.forEach(r -> r.setStatus(Reserva.Status.CANCELADA));
            reservaRepository.salvarTodas(reservasFuturas);
        }

        return CriarSalaUseCase.toResponse(salaRepository.salvar(sala));
    }
}
