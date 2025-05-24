package com.maicosmaniotto.pedidos_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.maicosmaniotto.pedidos_api.config.ValidationAdvice;
import com.maicosmaniotto.pedidos_api.data.ClienteDadosTeste;
import com.maicosmaniotto.pedidos_api.dto.ClienteRequest;
import com.maicosmaniotto.pedidos_api.dto.ClienteResponse;
import com.maicosmaniotto.pedidos_api.dto.PageDTO;
import com.maicosmaniotto.pedidos_api.dto.mapper.ClienteMapper;
import com.maicosmaniotto.pedidos_api.exception.RegistroNaoEncontradoException;
import com.maicosmaniotto.pedidos_api.model.Cliente;
import com.maicosmaniotto.pedidos_api.repository.ClienteRepository;

import jakarta.validation.ConstraintViolationException;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Spy
    private ClienteMapper clienteMapper = new ClienteMapper();

    @BeforeEach
    void setUp() throws Exception {
        ProxyFactory factory = new ProxyFactory(clienteService);
        factory.addAdvice(new ValidationAdvice());
        clienteService = (ClienteService) factory.getProxy();
    }

    @Test
    @DisplayName("Deve criar um cliente com sucesso quando dados válidos são fornecidos")
    void testeCriar() {
        ClienteRequest clienteRequest = ClienteDadosTeste.criarClienteRequestValidoComUmEndereco("Cliente 1");
        
        Cliente cliente = clienteMapper.toEntity(clienteRequest);
        cliente.setId(1L);

        ClienteResponse clienteResponse = clienteMapper.toResponse(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
      
        ClienteResponse clienteCriado = clienteService.criar(clienteRequest);
        assertEquals(clienteResponse, clienteCriado);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Não deve criar um cliente quando dados inválidos são fornecidos")
    void testeCriarDadosInvalidos() {
        // Cliente sem endereços
        ClienteRequest clienteRequest = ClienteDadosTeste.criarClienteRequestValidoComUmEndereco("Cliente 1");
        clienteRequest.enderecos().clear();
        assertThrows(ConstraintViolationException.class, () -> clienteService.criar(clienteRequest));
        verifyNoInteractions(clienteRepository);

        // Cliente com razão social vazia
        ClienteRequest clienteRequest2 = ClienteDadosTeste.criarClienteRequestValidoComUmEndereco(null);
        assertThrows(ConstraintViolationException.class, () -> clienteService.criar(clienteRequest2));
        verifyNoInteractions(clienteRepository);
    }    

    @Test
    @DisplayName("Deve atualizar um cliente com sucesso quando dados válidos são fornecidos")
    void testeAtualizar() {
        Cliente antigoCliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Cliente antigo");
        Cliente novoCliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Cliente novo");

        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(antigoCliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(novoCliente);

        ClienteRequest novoClienteRequest = ClienteDadosTeste.criarClienteRequestValidoComUmEndereco("Cliente novo");

        ClienteResponse clienteAtualizado = clienteService.atualizar(1L, novoClienteRequest);
        assertEquals(clienteMapper.toResponse(novoCliente), clienteAtualizado);
        verify(clienteRepository).save(any(Cliente.class));
        verify(clienteRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar RegistroNaoEncontradoException ao tentar atualizar um cliente que não existe")
    void testeAtualizarNaoEncontrado() {
        ClienteRequest clienteValidoRequest = ClienteDadosTeste.criarClienteRequestValidoComUmEndereco("Cliente novo");

        when(clienteRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(RegistroNaoEncontradoException.class, () -> clienteService.atualizar(123L, clienteValidoRequest));
        verify(clienteRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar ConstraintViolationException ao tentar atualizar um cliente com dados inválidos")
    void testeAtualizarDadosInvalidos() {
        ClienteRequest clienteRequestValido = ClienteDadosTeste.criarClienteRequestValidoComUmEndereco("Cliente novo");
        ClienteRequest clienteRequestInvalido1 = ClienteDadosTeste.criarClienteRequestValidoComUmEndereco("");

        ClienteRequest clienteRequestInvalido2 = ClienteDadosTeste.criarClienteRequestValidoComUmEndereco("Cliente sem endereços");
        clienteRequestInvalido2.enderecos().clear();

        // Id inválido e dados válidos
        assertThrows(ConstraintViolationException.class, () -> clienteService.atualizar(-1L, clienteRequestValido));
        // Id válido e dados inválidos
        assertThrows(ConstraintViolationException.class, () -> clienteService.atualizar(1L, clienteRequestInvalido1));
        assertThrows(ConstraintViolationException.class, () -> clienteService.atualizar(1L, clienteRequestInvalido2));
        verifyNoInteractions(clienteRepository);        
    }


    @Test
    @DisplayName("Deve excluir um cliente com sucesso")
    void testeExcluir() {
        Cliente cliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Cliente 1");
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
        clienteService.excluir(1L);
        verify(clienteRepository).delete(any(Cliente.class));
        verify(clienteRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar RegistroNaoEncontradoException ao tentar excluir um cliente que não existe")
    void testeExcluirIdNaoEncontrado() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RegistroNaoEncontradoException.class, () -> clienteService.excluir(1L));
        verify(clienteRepository).findById(anyLong());
    }    

    @Test
    @DisplayName("Deve lançar ConstraintViolationException ao tentar excluir um cliente com id inválido")
    void testeExcluirIdInvalido() {
        assertThrows(ConstraintViolationException.class, () -> clienteService.excluir(-1L));
        verifyNoInteractions(clienteRepository);
    }

    @Test
    @DisplayName("Deve retornar um cliente por id")
    void testeBuscarPorId() {
        ClienteRequest clienteRequest = ClienteDadosTeste.criarClienteRequestValidoComUmEndereco("Cliente");
        Cliente cliente = clienteMapper.toEntity(clienteRequest);
        cliente.setId(1L);
        ClienteResponse clienteResponseEsperado = clienteMapper.toResponse(cliente);

        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
        ClienteResponse clienteResponseEncontrado = clienteService.buscarPorId(1L);        
        assertEquals(clienteResponseEsperado, clienteResponseEncontrado);
        verify(clienteRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar RegistroNaoEncontradoException quando id não encontrado")
    void testeBuscarPorIdNaoEncontrado() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RegistroNaoEncontradoException.class, () -> clienteService.buscarPorId(1L));
        verify(clienteRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar ConstraintViolationException quando id inválido é fornecido")
    void testeBuscarPorIdInvalido() {
        assertThrows(ConstraintViolationException.class, () -> clienteService.buscarPorId(null));
        assertThrows(ConstraintViolationException.class, () -> clienteService.buscarPorId(0L));
        assertThrows(ConstraintViolationException.class, () -> clienteService.buscarPorId(-1L));
    }

    @Test
    @DisplayName("Deve retornar uma página de clientes com apenas um cliente e dois endereços")
    void testeListar() {
        Cliente cliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Cliente");
        ClienteDadosTeste.inserirEndereco(cliente, "Ruas 2", "1234");
        
        int pageNumber = 0;
        int pageSize = 50;

        Page<Cliente> mockPage = new PageImpl<>(Collections.singletonList(cliente));
        when(clienteRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        PageDTO<ClienteResponse> pageDTO = clienteService.listar(pageNumber, pageSize);
        
        verify(clienteRepository).findAll(any(PageRequest.class));      
        assertEquals(1, pageDTO.content().size());

        ClienteResponse clienteResponse = pageDTO.content().get(0);
        assertEquals(2, clienteResponse.enderecos().size());
    }
}
