package com.roomreservas.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record CriarReservaRequest(
        @NotNull(message = "ID do usuário é obrigatório")
        UUID usuarioId,

        @NotNull(message = "ID da sala é obrigatório")
        UUID salaId,

        @NotNull(message = "Data/hora de início é obrigatória")
        @Future(message = "Data de início deve ser futura")
        OffsetDateTime inicio,

        @NotNull(message = "Data/hora de fim é obrigatória")
        @Future(message = "Data de fim deve ser futura")
        OffsetDateTime fim
) {}
