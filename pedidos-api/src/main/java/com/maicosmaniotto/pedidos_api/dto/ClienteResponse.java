package com.maicosmaniotto.pedidos_api.dto;

import java.util.List;

public record ClienteResponse(
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
