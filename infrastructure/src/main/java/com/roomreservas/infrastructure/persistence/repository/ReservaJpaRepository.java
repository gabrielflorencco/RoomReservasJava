package com.roomreservas.infrastructure.persistence.repository;

import com.roomreservas.domain.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservaJpaRepository extends JpaRepository<Reserva, UUID> {

    List<Reserva> findByUsuarioId(UUID usuarioId);

    List<Reserva> findBySalaId(UUID salaId);

    @Query("""
        SELECT COUNT(r) FROM Reserva r
        WHERE r.usuario.id = :usuarioId
          AND r.status IN ('PENDENTE', 'CONFIRMADA')
    """)
    long countReservasAtivasByUsuarioId(@Param("usuarioId") UUID usuarioId);

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM Reserva r
        WHERE r.sala.id = :salaId
          AND r.status IN ('PENDENTE', 'CONFIRMADA')
          AND r.inicio < :fim
          AND r.fim > :inicio
          AND (:excluirId IS NULL OR r.id <> :excluirId)
    """)
    boolean existeConflitoDeHorario(
            @Param("salaId") UUID salaId,
            @Param("inicio") OffsetDateTime inicio,
            @Param("fim") OffsetDateTime fim,
            @Param("excluirId") UUID excluirId
    );

    @Query("""
        SELECT r FROM Reserva r
        WHERE r.sala.id = :salaId
          AND r.status IN ('PENDENTE', 'CONFIRMADA')
          AND r.inicio > :agora
    """)
    List<Reserva> findReservasFuturasAtivasBySalaId(
            @Param("salaId") UUID salaId,
            @Param("agora") OffsetDateTime agora
    );
}
