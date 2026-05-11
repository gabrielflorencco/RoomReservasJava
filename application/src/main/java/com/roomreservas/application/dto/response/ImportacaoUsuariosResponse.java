package com.roomreservas.application.dto.response;

import java.util.List;

public record ImportacaoUsuariosResponse(
    int inseridos,
    int ignorados,
    int falhas,
    List<String> erros
) {
}
