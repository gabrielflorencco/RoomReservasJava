package com.roomreservas.domain.exception;

public class RecursoNaoEncontradoException extends DomainException {
    public RecursoNaoEncontradoException(String recurso, Object id) {
        super(recurso + " com id '" + id + "' não encontrado(a).");
    }
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
