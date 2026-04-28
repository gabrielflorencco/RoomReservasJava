package com.roomreservas.application.usecase.sala;

import com.roomreservas.application.dto.request.AtualizarSalaRequest;
import com.roomreservas.application.dto.response.SalaResponse;
import com.roomreservas.domain.entity.Sala;
import com.roomreservas.domain.exception.RecursoNaoEncontradoException;
import com.roomreservas.domain.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AtualizarSalaUseCase {

    private final SalaRepository salaRepository;

    public SalaResponse executar(UUID id, AtualizarSalaRequest request) {
        Sala sala = salaRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sala", id));
        sala.setNome(request.nome().trim());
        sala.setCapacidade(request.capacidade());
        sala.setValorDiaria(request.valorDiaria());
        return CriarSalaUseCase.toResponse(salaRepository.salvar(sala));
    }
}
