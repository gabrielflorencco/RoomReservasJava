package com.roomreservas.domain.exception;

public class ReservaJaCanceladaException extends DomainException {
    public ReservaJaCanceladaException() {
        super("Esta reserva já foi cancelada.");
    }
}
