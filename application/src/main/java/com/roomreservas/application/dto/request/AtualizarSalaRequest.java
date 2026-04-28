package com.roomreservas.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record AtualizarSalaRequest(
        @NotBlank(message = "Nome da sala é obrigatório")
        @Size(min = 2, max = 100)
        String nome,

        @Min(value = 1, message = "Capacidade mínima é 1")
        @Max(value = 1000, message = "Capacidade máxima é 1000")
        int capacidade,

        @NotNull(message = "Valor da diária é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor da diária deve ser maior que zero")
        BigDecimal valorDiaria
) {}
