package com.roomreservas.infrastructure.persistence.adapter;

import com.roomreservas.domain.entity.Sala;
import com.roomreservas.domain.repository.SalaRepository;
import com.roomreservas.infrastructure.persistence.repository.SalaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SalaRepositoryAdapter implements SalaRepository {

    private final SalaJpaRepository jpaRepository;

    @Override public Sala salvar(Sala sala) { return jpaRepository.save(sala); }
    @Override public Optional<Sala> buscarPorId(UUID id) { return jpaRepository.findById(id); }
    @Override public List<Sala> listarTodas() { return jpaRepository.findAll(); }
    @Override public List<Sala> listarAtivas() { return jpaRepository.findByAtiva(true); }
    @Override public void deletar(UUID id) { jpaRepository.deleteById(id); }
    @Override public boolean existePorNome(String nome) { return jpaRepository.existsByNome(nome); }
}
