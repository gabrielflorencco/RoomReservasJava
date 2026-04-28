package com.roomreservas.presentation.controller;

import com.roomreservas.application.dto.request.AtualizarSalaRequest;
import com.roomreservas.application.dto.request.CriarSalaRequest;
import com.roomreservas.application.dto.response.SalaResponse;
import com.roomreservas.application.usecase.sala.*;
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
@RequestMapping("/api/v1/salas")
@RequiredArgsConstructor
@Tag(name = "Salas", description = "Gerenciamento de salas para reserva")
public class SalaController {

    private final CriarSalaUseCase criarSalaUseCase;
    private final BuscarSalaUseCase buscarSalaUseCase;
    private final AtualizarSalaUseCase atualizarSalaUseCase;
    private final AlternarStatusSalaUseCase alternarStatusSalaUseCase;

    @PostMapping
    @Operation(summary = "Criar sala")
    public ResponseEntity<SalaResponse> criar(@Valid @RequestBody CriarSalaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(criarSalaUseCase.executar(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sala por ID")
    public ResponseEntity<SalaResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(buscarSalaUseCase.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas as salas")
    public ResponseEntity<List<SalaResponse>> listarTodas() {
        return ResponseEntity.ok(buscarSalaUseCase.listarTodas());
    }

    @GetMapping("/ativas")
    @Operation(summary = "Listar salas ativas")
    public ResponseEntity<List<SalaResponse>> listarAtivas() {
        return ResponseEntity.ok(buscarSalaUseCase.listarAtivas());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sala")
    public ResponseEntity<SalaResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarSalaRequest request) {
        return ResponseEntity.ok(atualizarSalaUseCase.executar(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Ativar/desativar sala", description = "Alterna o status ativo/inativo da sala. Ao desativar, cancela automaticamente reservas futuras.")
    public ResponseEntity<SalaResponse> alternarStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(alternarStatusSalaUseCase.executar(id));
    }
}
