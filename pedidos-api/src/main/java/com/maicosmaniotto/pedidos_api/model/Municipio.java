package com.maicosmaniotto.pedidos_api.model;

import com.maicosmaniotto.pedidos_api.enums.UnidadeFederativa;
import com.maicosmaniotto.pedidos_api.enums.converters.UnidadeFederativaConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "municipios")
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @Column(name = "codigo_ibge", length = 150, nullable = true)
    private String codigoIbge;

    @NotNull
    @Convert(converter = UnidadeFederativaConverter.class)
    @Column(name = "uf", length = 2, nullable = false)
    private UnidadeFederativa uf;

}
