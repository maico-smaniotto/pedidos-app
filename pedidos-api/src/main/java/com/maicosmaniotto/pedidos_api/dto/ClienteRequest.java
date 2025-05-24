package com.maicosmaniotto.pedidos_api.dto;

import java.util.List;

import com.maicosmaniotto.pedidos_api.enums.StatusRegistro;
import com.maicosmaniotto.pedidos_api.enums.TipoPessoa;
import com.maicosmaniotto.pedidos_api.enums.validation.TipoPessoaStringSubset;
import com.maicosmaniotto.pedidos_api.enums.validation.StatusRegistroStringSubset;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClienteRequest(

    @NotNull
    @NotBlank(message = "tipo de pessoa é obrigatório")
    @TipoPessoaStringSubset(anyOf = {TipoPessoa.FISICA, TipoPessoa.JURIDICA})
    String tipoPessoa,

    @NotNull
    @NotBlank(message = "CPF/CNPJ é obrigatório")
    @Size(min = 1, max = 21)
    String cpfCnpj,

    @NotNull
    @NotBlank(message = "razão social é obrigatória")
    @Size(min = 1, max = 150, message = "deve ter entre 1 e 150 caracteres")
    String razaoSocial, 

    @NotNull
    @NotBlank(message = "nome fantasia é obrigatório")
    @Size(min = 1, max = 150, message = "deve ter entre 1 e 150 caracteres")
    String nomeFantasia, 

    @NotNull
    @NotBlank(message = "e-mail é obrigatório")
    @Size(min = 1, max = 150)
    @Email(message = "e-mail inválido")
    String email, 

    @StatusRegistroStringSubset(anyOf = {StatusRegistro.ATIVO, StatusRegistro.INATIVO})
    String statusRegistro,

    @NotNull
    @NotEmpty
    @Valid
    List<ClienteEnderecoDTO> enderecos
) { }
