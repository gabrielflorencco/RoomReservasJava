package com.roomreservas.application.dto.response;

import com.roomreservas.domain.entity.Reserva;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ReservaResponse(
        UUID id, UUID usuarioId, String nomeUsuario,
        UUID salaId, String nomeSala,
        OffsetDateTime inicio, OffsetDateTime fim,
        BigDecimal valorTotal, Reserva.Status status, OffsetDateTime dataCriacao
) {}
