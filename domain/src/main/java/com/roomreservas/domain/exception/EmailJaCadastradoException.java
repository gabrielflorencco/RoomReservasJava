package com.roomreservas.domain.exception;

public class EmailJaCadastradoException extends DomainException {
    public EmailJaCadastradoException(String email) {
        super("Email '" + email + "' já está cadastrado.");
    }
}
