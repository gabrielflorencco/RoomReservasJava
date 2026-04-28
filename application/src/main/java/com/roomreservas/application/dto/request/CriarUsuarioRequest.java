package com.roomreservas.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record CriarUsuarioRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email
) {}
