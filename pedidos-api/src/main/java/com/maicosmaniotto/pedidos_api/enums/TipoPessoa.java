package com.maicosmaniotto.pedidos_api.enums;

public enum TipoPessoa {
    FISICA   ('F', "Física"), 
    JURIDICA ('J', "Jurídica");

    private char valor;
    private String descricao;

    private TipoPessoa(char valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public Character getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return descricao;
    }

}
