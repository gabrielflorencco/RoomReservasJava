package com.roomreservas.presentation.controller;

import com.roomreservas.application.dto.request.CriarReservaRequest;
import com.roomreservas.application.dto.response.ReservaResponse;
import com.roomreservas.application.usecase.reserva.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Gerenciamento de reservas de salas")
public class ReservaController {

    private final CriarReservaUseCase criarReservaUseCase;
    private final BuscarReservaUseCase buscarReservaUseCase;
    private final CancelarReservaUseCase cancelarReservaUseCase;
    private final ConfirmarReservaUseCase confirmarReservaUseCase;

    @PostMapping
    @Operation(summary = "Criar reserva", description = "Cria uma nova reserva. Valida conflito de horários, sala ativa e limite de 3 reservas por usuário.")
    public ResponseEntity<ReservaResponse> criar(@Valid @RequestBody CriarReservaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(criarReservaUseCase.executar(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por ID")
    public ResponseEntity<ReservaResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(buscarReservaUseCase.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas as reservas")
    public ResponseEntity<List<ReservaResponse>> listarTodas() {
        return ResponseEntity.ok(buscarReservaUseCase.listarTodas());
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar reservas por usuário")
    public ResponseEntity<List<ReservaResponse>> listarPorUsuario(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(buscarReservaUseCase.listarPorUsuario(usuarioId));
    }

    @GetMapping("/sala/{salaId}")
    @Operation(summary = "Listar reservas por sala")
    public ResponseEntity<List<ReservaResponse>> listarPorSala(@PathVariable UUID salaId) {
        return ResponseEntity.ok(buscarReservaUseCase.listarPorSala(salaId));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar reserva", description = "Cancela a reserva. Só é permitido com pelo menos 24h de antecedência.")
    public ResponseEntity<ReservaResponse> cancelar(@PathVariable UUID id) {
        return ResponseEntity.ok(cancelarReservaUseCase.executar(id));
    }

    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar reserva", description = "Confirma uma reserva com status PENDENTE.")
    public ResponseEntity<ReservaResponse> confirmar(@PathVariable UUID id) {
        return ResponseEntity.ok(confirmarReservaUseCase.executar(id));
    }
}
