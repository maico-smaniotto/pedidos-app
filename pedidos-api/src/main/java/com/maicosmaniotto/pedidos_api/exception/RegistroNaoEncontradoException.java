package com.maicosmaniotto.pedidos_api.exception;

public class RegistroNaoEncontradoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RegistroNaoEncontradoException(Long id) {
        super("Record not found with id: " + id);
    }

}
