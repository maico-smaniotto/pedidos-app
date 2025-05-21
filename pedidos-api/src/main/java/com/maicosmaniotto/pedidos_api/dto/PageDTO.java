package com.maicosmaniotto.pedidos_api.dto;

import java.util.List;

public record PageDTO<T>(
    List<T> content,
    long totalElements,
    int totalPages,
    int pageSize,
    int page
) { }
