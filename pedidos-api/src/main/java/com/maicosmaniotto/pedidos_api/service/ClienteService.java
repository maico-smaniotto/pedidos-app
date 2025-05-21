package com.maicosmaniotto.pedidos_api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.maicosmaniotto.pedidos_api.dto.ClienteRequest;
import com.maicosmaniotto.pedidos_api.dto.ClienteResponse;
import com.maicosmaniotto.pedidos_api.dto.PageDTO;
import com.maicosmaniotto.pedidos_api.dto.mapper.ClienteMapper;
import com.maicosmaniotto.pedidos_api.enums.converters.TipoPessoaConverter;
import com.maicosmaniotto.pedidos_api.enums.converters.StatusRegistroConverter;
import com.maicosmaniotto.pedidos_api.exception.RegistroNaoEncontradoException;
import com.maicosmaniotto.pedidos_api.model.Cliente;
import com.maicosmaniotto.pedidos_api.repository.ClienteRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Validated
@Service
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public PageDTO<ClienteResponse> list(
        @PositiveOrZero    int pageNumber, 
        @Positive @Max(50) int pageSize
    ) {
        Page<Cliente> page = clienteRepository.findAll(PageRequest.of(pageNumber, pageSize));
        List<ClienteResponse> clientes = page.stream().map(clienteMapper::toResponse).toList();
        return new PageDTO<>(clientes, page.getTotalElements(), page.getTotalPages(), page.getSize(), page.getNumber());
    }

    public ClienteResponse findById(@NotNull @Positive Long id) {
        return clienteRepository.findById(id).map(clienteMapper::toResponse)
            .orElseThrow(() -> new RegistroNaoEncontradoException(id));
    }

    public ClienteResponse create(@Valid @NotNull ClienteRequest clienteRequest) {
        return clienteMapper.toResponse(clienteRepository.save(clienteMapper.toEntity(clienteRequest)));
    }

    public ClienteResponse update(@NotNull @Positive Long id, @Valid @NotNull ClienteRequest clienteRequest) {
        return clienteRepository.findById(id)
            .map(found -> {
                found.setTipoPessoa(TipoPessoaConverter.stringToEntityAttribute(clienteRequest.tipoPessoa()));
                found.setRazaoSocial(clienteRequest.razaoSocial());
                found.setNomeFantasia(clienteRequest.nomeFantasia());
                found.setCpfCnpj(clienteRequest.cpfCnpj());

                if (clienteRequest.statusRegistro() != null) {
                    found.setStatusRegistro(StatusRegistroConverter.stringToEntityAttribute(clienteRequest.statusRegistro()));
                }
                // Não pode sobrescrever a lista com .setEnderecos() pois o Hibernate precisa da referência original para funcionar
                // found.setEnderecos(mapper.toEntity(clienteRequest).getEnderecos());
                found.getEnderecos().clear();
                clienteMapper.toEntity(clienteRequest).getEnderecos().forEach(found.getEnderecos()::add);                
                return clienteMapper.toResponse(clienteRepository.save(found));
            }).orElseThrow(() -> new RegistroNaoEncontradoException(id));
    }

    public void delete(@NotNull @Positive Long id) {
        clienteRepository.delete(
            clienteRepository.findById(id)
            .orElseThrow(() -> new RegistroNaoEncontradoException(id))
        );
    }
    
}
