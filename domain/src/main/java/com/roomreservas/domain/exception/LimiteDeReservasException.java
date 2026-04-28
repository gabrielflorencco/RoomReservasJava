package com.roomreservas.domain.exception;

public class LimiteDeReservasException extends DomainException {
    public LimiteDeReservasException() {
        super("O usuário atingiu o limite máximo de 3 reservas ativas simultâneas.");
    }
}
