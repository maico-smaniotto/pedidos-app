package com.maicosmaniotto.pedidos_api.dto;

public record UsuarioAuthentication(
    String username,
    String password
) { }