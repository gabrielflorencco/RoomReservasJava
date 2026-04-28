package com.roomreservas.domain.exception;

public class ConflitoDeHorarioException extends DomainException {
    public ConflitoDeHorarioException() {
        super("A sala já possui uma reserva confirmada ou pendente neste período.");
    }
}
