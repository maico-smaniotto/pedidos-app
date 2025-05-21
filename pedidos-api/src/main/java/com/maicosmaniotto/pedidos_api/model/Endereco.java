package com.maicosmaniotto.pedidos_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class Endereco {

    @NotNull
    @NotBlank
    @Column(name = "logradouro", length = 100, nullable = false)
    private String logradouro;

    @NotNull
    @NotBlank
    @Column(name = "numero", length = 20, nullable = false)
    private String numero;

    @NotNull
    @NotBlank
    @Column(name = "complemento", length = 100, nullable = false)
    private String complemento;

    @NotNull
    @NotBlank
    @Column(name = "bairro", length = 100, nullable = false)
    private String bairro;

    @NotNull    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;

    @NotNull
    @NotBlank
    @Column(name = "codigo_postal", length = 10, nullable = false)
    private String codigoPostal;

}
