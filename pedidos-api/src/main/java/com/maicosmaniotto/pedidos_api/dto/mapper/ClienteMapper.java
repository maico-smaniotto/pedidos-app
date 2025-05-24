package com.maicosmaniotto.pedidos_api.dto.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.maicosmaniotto.pedidos_api.dto.ClienteRequest;
import com.maicosmaniotto.pedidos_api.dto.ClienteResponse;
import com.maicosmaniotto.pedidos_api.dto.ClienteEnderecoDTO;
import com.maicosmaniotto.pedidos_api.enums.converters.TipoPessoaConverter;
import com.maicosmaniotto.pedidos_api.enums.converters.UnidadeFederativaConverter;
import com.maicosmaniotto.pedidos_api.enums.converters.StatusRegistroConverter;
import com.maicosmaniotto.pedidos_api.model.Cliente;
import com.maicosmaniotto.pedidos_api.model.ClienteEndereco;
import com.maicosmaniotto.pedidos_api.model.Municipio;

@Component
public class ClienteMapper {

    public ClienteResponse toResponse(Cliente entity) {

        if (entity == null) {
            return null;
        }
        
        List<ClienteEnderecoDTO> enderecos = entity.getEnderecos()
            .stream()
            .map(endereco -> new ClienteEnderecoDTO(
                endereco.getId(), 
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getMunicipio().getId(),
                endereco.getMunicipio().getNome(),
                endereco.getMunicipio().getCodigoIbge(),
                endereco.getMunicipio().getUf().name(),
                endereco.getCodigoPostal()))
            .toList();
        
        return new ClienteResponse(
            entity.getId(), 
            entity.getTipoPessoa().getValor().toString(), 
            entity.getCpfCnpj(), 
            entity.getRazaoSocial(), 
            entity.getNomeFantasia(),
            entity.getEmail(),
            entity.getStatusRegistro().toString(),
            enderecos
        );

    }

    public Cliente toEntity(ClienteRequest clienteRequest) {
        if (clienteRequest == null) {
            return null;
        }
        
        Cliente cliente = new Cliente();
    
        cliente.setTipoPessoa(TipoPessoaConverter.stringToEntityAttribute(clienteRequest.tipoPessoa()));        
        cliente.setCpfCnpj(clienteRequest.cpfCnpj());
        cliente.setRazaoSocial(clienteRequest.razaoSocial());
        cliente.setNomeFantasia(clienteRequest.nomeFantasia());
        cliente.setEmail(clienteRequest.email());

        cliente.getEnderecos().clear();
        clienteRequest.enderecos().stream().forEach(enderecoRequest -> {
            var endereco = new ClienteEndereco();
            endereco.setId(enderecoRequest.id());
            endereco.setLogradouro(enderecoRequest.logradouro());
            endereco.setNumero(enderecoRequest.numero());
            endereco.setComplemento(enderecoRequest.complemento());
            endereco.setBairro(enderecoRequest.bairro());
            endereco.setCodigoPostal(enderecoRequest.codigoPostal());

            Municipio municipio = new Municipio();
            municipio.setId(enderecoRequest.municipioId());
            municipio.setNome(enderecoRequest.municipioNome());
            municipio.setCodigoIbge(enderecoRequest.municipioCodigoIbge());
            
            municipio.setUf(
                new UnidadeFederativaConverter()
                    .convertToEntityAttribute(enderecoRequest.uf())
            );

            endereco.setMunicipio(municipio);

            endereco.setCliente(cliente);

            cliente.getEnderecos().add(endereco);
        });
        
        if (clienteRequest.statusRegistro() != null) {
            cliente.setStatusRegistro(StatusRegistroConverter.stringToEntityAttribute(clienteRequest.statusRegistro()));
        }
        
        return cliente;
    }

}
