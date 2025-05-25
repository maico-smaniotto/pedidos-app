package com.maicosmaniotto.pedidos_api.dto;

public record UsuarioAuthenticationRequest(
    String username,
    String password
) { }