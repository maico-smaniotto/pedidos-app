package com.maicosmaniotto.pedidos_api.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maicosmaniotto.pedidos_api.enums.TipoPessoa;
import com.maicosmaniotto.pedidos_api.enums.StatusRegistro;
import com.maicosmaniotto.pedidos_api.enums.converters.TipoPessoaConverter;
import com.maicosmaniotto.pedidos_api.enums.converters.StatusRegistroConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name = "clientes")
@SQLDelete(sql = "update clientes set status_registro = 'X' where id = ?")
@SQLRestriction("status_registro <> 'X'")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("_id")
    private Long id;

    @NotNull
    @Convert(converter = TipoPessoaConverter.class)
    @Column(name = "tipo_pessoa", nullable = false)
    private TipoPessoa tipoPessoa;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 21)
    @Column(name = "cpf_cnpj", length = 21, nullable = false)
    private String cpfCnpj;    

    @NotNull
    @NotBlank
    @Size(min = 1, max = 150)
    @Column(name = "razao_social", length = 150, nullable = false)
    private String razaoSocial;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 150)
    @Column(name = "nome_fantasia", length = 150, nullable = false)
    private String nomeFantasia;

    @NotNull
    @NotBlank
    @Email
    @Column(name = "email", length = 150, nullable = false)
    private String email;

    @NotNull
    @Convert(converter = StatusRegistroConverter.class)
    @Column(name = "status_registro", length = 1, nullable = false)
    private StatusRegistro statusRegistro = StatusRegistro.ATIVO;

    @Setter(AccessLevel.NONE)
    @NotNull
    @NotEmpty
    @Valid
    // mappedBy = nome da própria entidade (classe) com letra minúscula (o nome que teria a tabela caso não fosse definido com @Table)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "id")
    private List<ClienteEndereco> enderecos = new ArrayList<>();

}
