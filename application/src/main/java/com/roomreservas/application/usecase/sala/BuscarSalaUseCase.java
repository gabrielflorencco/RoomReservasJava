package com.roomreservas.application.usecase.sala;

import com.roomreservas.application.dto.response.SalaResponse;
import com.roomreservas.domain.entity.Sala;
import com.roomreservas.domain.exception.RecursoNaoEncontradoException;
import com.roomreservas.domain.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuscarSalaUseCase {

    private final SalaRepository salaRepository;

    public SalaResponse buscarPorId(UUID id) {
        Sala sala = salaRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sala", id));
        return CriarSalaUseCase.toResponse(sala);
    }

    public List<SalaResponse> listarTodas() {
        return salaRepository.listarTodas().stream().map(CriarSalaUseCase::toResponse).toList();
    }

    public List<SalaResponse> listarAtivas() {
        return salaRepository.listarAtivas().stream().map(CriarSalaUseCase::toResponse).toList();
    }
}
