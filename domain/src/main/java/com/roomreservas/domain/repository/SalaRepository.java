package com.roomreservas.domain.repository;

import com.roomreservas.domain.entity.Sala;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalaRepository {
    Sala salvar(Sala sala);
    Optional<Sala> buscarPorId(UUID id);
    List<Sala> listarTodas();
    List<Sala> listarAtivas();
    void deletar(UUID id);
    boolean existePorNome(String nome);
}
