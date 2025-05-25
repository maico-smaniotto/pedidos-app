package com.maicosmaniotto.pedidos_api.dto;

import com.maicosmaniotto.pedidos_api.enums.UserRole;

public record UsuarioRegisterRequest(
    String username,
    String password,
    UserRole role
) { }
