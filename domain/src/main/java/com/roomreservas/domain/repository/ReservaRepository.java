package com.roomreservas.domain.repository;

import com.roomreservas.domain.entity.Reserva;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservaRepository {
    Reserva salvar(Reserva reserva);
    Optional<Reserva> buscarPorId(UUID id);
    List<Reserva> listarTodas();
    List<Reserva> listarPorUsuario(UUID usuarioId);
    List<Reserva> listarPorSala(UUID salaId);

    /**
     * Verifica se existe conflito de horário para determinada sala,
     * excluindo opcionalmente uma reserva (útil para edição).
     */
    boolean existeConflitoDeHorario(UUID salaId, OffsetDateTime inicio, OffsetDateTime fim, UUID excluirReservaId);

    /**
     * Conta reservas ativas (PENDENTE ou CONFIRMADA) de um usuário.
     */
    long contarReservasAtivasPorUsuario(UUID usuarioId);

    /**
     * Lista reservas futuras e ativas de uma sala (para cancelamento em cascata ao desativar sala).
     */
    List<Reserva> listarReservasFuturasAtivasPorSala(UUID salaId);

    void salvarTodas(List<Reserva> reservas);
}
