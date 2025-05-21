package com.maicosmaniotto.pedidos_api.exception;

public class UsuarioJaExisteException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UsuarioJaExisteException(String username) {
        super("Já existe um usuário com o nome \"" + username + "\"");
    }

    public UsuarioJaExisteException() {
        super("Já existe um usuário com o nome informado");
    }
}
