package com.maicosmaniotto.pedidos_api.data;

import java.util.List;

import com.maicosmaniotto.pedidos_api.dto.ClienteEnderecoDTO;
import com.maicosmaniotto.pedidos_api.dto.ClienteRequest;
import com.maicosmaniotto.pedidos_api.enums.StatusRegistro;
import com.maicosmaniotto.pedidos_api.enums.TipoPessoa;
import com.maicosmaniotto.pedidos_api.enums.UnidadeFederativa;
import com.maicosmaniotto.pedidos_api.model.Cliente;
import com.maicosmaniotto.pedidos_api.model.ClienteEndereco;
import com.maicosmaniotto.pedidos_api.model.Municipio;

public class ClienteDadosTeste {
    
    public static Cliente criarClienteValidoComUmEndereco(String razaoSocial, Municipio municipio) {
        Cliente cliente = new Cliente();
        cliente.setTipoPessoa(TipoPessoa.FISICA);
        cliente.setCpfCnpj("12345678901");
        cliente.setRazaoSocial(razaoSocial);
        cliente.setNomeFantasia("Cliente Fantasia");
        cliente.setEmail("cliente1@example.com");
        cliente.setStatusRegistro(StatusRegistro.ATIVO);

        inserirEndereco(cliente, "Rua Fulano", "123", municipio);
        return cliente;
    }

    public static ClienteRequest criarClienteRequestDadosValidosComUmEndereco(String razaoSocial) {
        return new ClienteRequest(
            TipoPessoa.FISICA.toString(),
            "12345678901",
            razaoSocial,
            "Cliente Fantasia",
            "cliente1@example.com",
            StatusRegistro.ATIVO.toString(),
            List.of(
                new ClienteEnderecoDTO(
                    null,
                    "Rua Fulano",
                    "123",
                    "Complemento",
                    "Bairro",
                    1L,
                    "São Paulo",
                    "1234567",
                    UnidadeFederativa.SP.name(),
                    "12345678"
                )
            )
        );
    }

    public static ClienteRequest criarClienteRequestDadosValidosSemEndereco(String razaoSocial) {
        return new ClienteRequest(
            TipoPessoa.FISICA.toString(),
            "12345678901",
            razaoSocial,
            "Cliente Fantasia",
            "cliente1@example.com",
            StatusRegistro.ATIVO.toString(),
            List.of()
        );
    }

    public static void inserirEndereco(Cliente cliente, String logradouro, String numero, Municipio municipio) {
        ClienteEndereco endereco = new ClienteEndereco();        
        endereco.setLogradouro(logradouro);
        endereco.setNumero(numero);
        endereco.setComplemento("Complemento");
        endereco.setBairro("Bairro");
        endereco.setCodigoPostal("12345678");
        endereco.setMunicipio(municipio);
        endereco.setCliente(cliente);
        cliente.getEnderecos().add(endereco);
    }

    public static Municipio criarMunicipioValido() {
        return new Municipio(
            null, 
            "São Paulo", 
            "1234567",
            UnidadeFederativa.SP
        );
    }

}
