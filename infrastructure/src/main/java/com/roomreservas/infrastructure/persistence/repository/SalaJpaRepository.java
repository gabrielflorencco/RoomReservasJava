package com.roomreservas.infrastructure.persistence.repository;

import com.roomreservas.domain.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SalaJpaRepository extends JpaRepository<Sala, UUID> {
    List<Sala> findByAtiva(boolean ativa);
    boolean existsByNome(String nome);
}
