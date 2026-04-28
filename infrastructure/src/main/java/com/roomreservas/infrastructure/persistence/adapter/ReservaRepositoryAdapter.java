package com.roomreservas.infrastructure.persistence.adapter;

import com.roomreservas.domain.entity.Reserva;
import com.roomreservas.domain.repository.ReservaRepository;
import com.roomreservas.infrastructure.persistence.repository.ReservaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservaRepositoryAdapter implements ReservaRepository {

    private final ReservaJpaRepository jpaRepository;

    @Override public Reserva salvar(Reserva reserva) { return jpaRepository.save(reserva); }
    @Override public Optional<Reserva> buscarPorId(UUID id) { return jpaRepository.findById(id); }
    @Override public List<Reserva> listarTodas() { return jpaRepository.findAll(); }
    @Override public List<Reserva> listarPorUsuario(UUID usuarioId) { return jpaRepository.findByUsuarioId(usuarioId); }
    @Override public List<Reserva> listarPorSala(UUID salaId) { return jpaRepository.findBySalaId(salaId); }

    @Override
    public boolean existeConflitoDeHorario(UUID salaId, OffsetDateTime inicio, OffsetDateTime fim, UUID excluirReservaId) {
        return jpaRepository.existeConflitoDeHorario(salaId, inicio, fim, excluirReservaId);
    }

    @Override
    public long contarReservasAtivasPorUsuario(UUID usuarioId) {
        return jpaRepository.countReservasAtivasByUsuarioId(usuarioId);
    }

    @Override
    public List<Reserva> listarReservasFuturasAtivasPorSala(UUID salaId) {
        return jpaRepository.findReservasFuturasAtivasBySalaId(salaId, OffsetDateTime.now());
    }

    @Override
    public void salvarTodas(List<Reserva> reservas) {
        jpaRepository.saveAll(reservas);
    }
}
