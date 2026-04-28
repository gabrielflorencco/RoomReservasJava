package com.roomreservas.domain.exception;

public class SalaInativaException extends DomainException {
    public SalaInativaException() {
        super("Não é possível realizar reserva em uma sala inativa.");
    }
}
