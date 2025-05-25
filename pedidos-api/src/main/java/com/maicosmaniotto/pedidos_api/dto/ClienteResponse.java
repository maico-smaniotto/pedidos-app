package com.maicosmaniotto.pedidos_api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClienteResponse(
    
    @JsonProperty("_id")
    Long id,
    
    String tipoPessoa,
    
    String docPessoa,
    
    String razaoSocial,
    
    String nomeFantasia,
    
    String email,
    
    String statusRegistro,
    
    List<ClienteEnderecoDTO> enderecos
    
) {

}
