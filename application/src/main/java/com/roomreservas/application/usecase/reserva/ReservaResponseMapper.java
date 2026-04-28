package com.roomreservas.application.usecase.reserva;

import com.roomreservas.application.dto.response.ReservaResponse;
import com.roomreservas.domain.entity.Reserva;

public final class ReservaResponseMapper {

    private ReservaResponseMapper() {}

    public static ReservaResponse toResponse(Reserva r) {
        return new ReservaResponse(
                r.getId(),
                r.getUsuario().getId(),
                r.getUsuario().getNome(),
                r.getSala().getId(),
                r.getSala().getNome(),
                r.getInicio(),
                r.getFim(),
                r.getValorTotal(),
                r.getStatus(),
                r.getDataCriacao()
        );
    }
}
