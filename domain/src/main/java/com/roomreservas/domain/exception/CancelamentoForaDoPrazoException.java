package com.roomreservas.domain.exception;

public class CancelamentoForaDoPrazoException extends DomainException {
    public CancelamentoForaDoPrazoException() {
        super("O cancelamento só é permitido com pelo menos 24 horas de antecedência.");
    }
}
