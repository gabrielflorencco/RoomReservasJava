package com.roomreservas.presentation.controller;

import com.roomreservas.application.dto.request.AtualizarUsuarioRequest;
import com.roomreservas.application.dto.request.CriarUsuarioRequest;
import com.roomreservas.application.dto.response.ImportacaoUsuariosResponse;
import com.roomreservas.application.dto.response.UsuarioResponse;
import com.roomreservas.application.usecase.usuario.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema")
public class UsuarioController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final BuscarUsuarioUseCase buscarUsuarioUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;
    private final ImportarUsuariosUseCase importarUsuariosUseCase;

    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cadastra um novo usuário no sistema")
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody CriarUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(criarUsuarioUseCase.executar(request));
    }

    @PostMapping(value = "/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Importar usuários a partir de uma planilha Excel", description = "Cadastra novos usuários no sistema a partir de uma planilha Excel.")
    public ResponseEntity<ImportacaoUsuariosResponse> importar(@RequestParam("arquivo") MultipartFile arquivo) {
        try {

            ImportacaoUsuariosResponse response =
                    importarUsuariosUseCase.executar(
                            arquivo.getInputStream()
                    );

            return ResponseEntity.ok(response);

        } catch (IOException ex) {

            throw new RuntimeException(
                "Erro ao ler arquivo enviado",
                ex
            );
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(buscarUsuarioUseCase.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(buscarUsuarioUseCase.listarTodos());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário")
    public ResponseEntity<UsuarioResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarUsuarioRequest request) {
        return ResponseEntity.ok(atualizarUsuarioUseCase.executar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        deletarUsuarioUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
}
