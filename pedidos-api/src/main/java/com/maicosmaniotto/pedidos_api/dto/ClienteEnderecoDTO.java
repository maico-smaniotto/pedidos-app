package com.maicosmaniotto.pedidos_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClienteEnderecoDTO(
    
    @JsonProperty("_id")
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
