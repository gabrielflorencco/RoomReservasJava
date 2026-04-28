package com.roomreservas.application.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record SalaResponse(UUID id, String nome, int capacidade, boolean ativa, BigDecimal valorDiaria) {}
