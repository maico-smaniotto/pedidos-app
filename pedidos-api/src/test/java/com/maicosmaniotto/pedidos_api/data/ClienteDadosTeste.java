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
    
    public static Cliente criarClienteValidoComUmEndereco(String razaoSocial) {
        Cliente cliente = new Cliente();
        cliente.setRazaoSocial(razaoSocial);
        cliente.setTipoPessoa(TipoPessoa.FISICA);
        inserirEndereco(cliente, "Rua Fulado", "123");
        return cliente;
    }

    public static ClienteRequest criarClienteRequestValidoComUmEndereco(String razaoSocial) {
        return new ClienteRequest(
            TipoPessoa.FISICA.getValor().toString(),
            "12345678901",
            razaoSocial,
            "Cliente 1",
            "cliente1@example.com",
            StatusRegistro.ATIVO.toString(),
            List.of(
                new ClienteEnderecoDTO(
                    null,
                    "Rua Fulado",
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

    public static void inserirEndereco(Cliente cliente, String logradouro, String numero) {
        ClienteEndereco endereco = new ClienteEndereco();        
        endereco.setLogradouro(logradouro);
        endereco.setNumero(numero);
        endereco.setComplemento("Complemento");
        endereco.setBairro("Bairro");
        endereco.setCodigoPostal("12345678");
        endereco.setMunicipio(criarMunicipioValido());
        endereco.setCliente(cliente);
        cliente.getEnderecos().add(endereco);
    }

    public static Municipio criarMunicipioValido() {
        return new Municipio(
            1L, 
            "São Paulo", 
            "1234567", // Código IBGE fictício
            UnidadeFederativa.SP
        );
    }

}
