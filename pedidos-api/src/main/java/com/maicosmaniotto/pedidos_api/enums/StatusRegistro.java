package com.maicosmaniotto.pedidos_api.enums;

public enum StatusRegistro {
    ATIVO    ('A', "Ativo"),
    INATIVO  ('I', "Inativo"),
    EXCLUIDO ('X', "Excluído");

    private final char valor;
    private final String descricao;

    private StatusRegistro(char valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public char getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return descricao;
    }
    
}
