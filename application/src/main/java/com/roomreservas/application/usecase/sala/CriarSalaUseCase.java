package com.roomreservas.application.usecase.sala;

import com.roomreservas.application.dto.request.CriarSalaRequest;
import com.roomreservas.application.dto.response.SalaResponse;
import com.roomreservas.domain.entity.Sala;
import com.roomreservas.domain.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarSalaUseCase {

    private final SalaRepository salaRepository;

    public SalaResponse executar(CriarSalaRequest request) {
        Sala sala = Sala.builder()
                .nome(request.nome().trim())
                .capacidade(request.capacidade())
                .valorDiaria(request.valorDiaria())
                .ativa(true)
                .build();
        return toResponse(salaRepository.salvar(sala));
    }

    public static SalaResponse toResponse(Sala s) {
        return new SalaResponse(s.getId(), s.getNome(), s.getCapacidade(), s.isAtiva(), s.getValorDiaria());
    }
}
