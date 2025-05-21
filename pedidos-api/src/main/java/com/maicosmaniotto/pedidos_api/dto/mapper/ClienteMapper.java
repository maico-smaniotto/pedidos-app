package com.maicosmaniotto.pedidos_api.dto.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.maicosmaniotto.pedidos_api.dto.ClienteRequest;
import com.maicosmaniotto.pedidos_api.dto.ClienteResponse;
import com.maicosmaniotto.pedidos_api.dto.ClienteEnderecoDTO;
import com.maicosmaniotto.pedidos_api.enums.converters.TipoPessoaConverter;
import com.maicosmaniotto.pedidos_api.enums.converters.StatusRegistroConverter;
import com.maicosmaniotto.pedidos_api.model.Cliente;

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
        
        // if (clienteRequest.id() != null) {
        //     cliente.setId(clienteRequest.id());
        // }        
        
        cliente.setTipoPessoa(TipoPessoaConverter.stringToEntityAttribute(clienteRequest.tipoPessoa()));        
        cliente.setRazaoSocial(clienteRequest.razaoSocial());
        cliente.setNomeFantasia(clienteRequest.nomeFantasia());
        
        
        if (clienteRequest.statusRegistro() != null) {
            cliente.setStatusRegistro(StatusRegistroConverter.stringToEntityAttribute(clienteRequest.statusRegistro()));
        }

        // cliente.getEnderecos().clear();
        // clienteRequest.enderecos().stream().forEach(enderecoRequest -> {
        //     var endereco = new ClienteEndereco();
        //     endereco.setId(enderecoRequest.id());
        //     lesson.setTitle(enderecoRequest.title());
        //     lesson.setVideoCode(enderecoRequest.videoCode());
        //     lesson.setCliente(cliente);

        //     cliente.getEderecos().add(endereco);
        // });
        
        return cliente;
    }

}
