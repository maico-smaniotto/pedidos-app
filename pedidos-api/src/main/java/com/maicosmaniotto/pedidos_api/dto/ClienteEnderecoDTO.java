package com.maicosmaniotto.pedidos_api.dto;

public record ClienteEnderecoDTO(
    Long id,
    String logradouro,
    String numero,
    String complemento,
    String bairro,
    Long municipioId,
    String municipioNome,
    String municipioCodigoIbge,
    String uf,
    String codigoPostal
) {

}
