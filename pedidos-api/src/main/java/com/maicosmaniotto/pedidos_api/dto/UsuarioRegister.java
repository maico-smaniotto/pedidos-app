package com.maicosmaniotto.pedidos_api.dto;

import com.maicosmaniotto.pedidos_api.enums.UserRole;

public record UsuarioRegister(
    String username,
    String password,
    UserRole role
) { }
