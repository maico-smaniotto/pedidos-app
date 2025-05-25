package com.maicosmaniotto.pedidos_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicosmaniotto.pedidos_api.config.ValidationAdvice;
import com.maicosmaniotto.pedidos_api.data.ClienteDadosTeste;
import com.maicosmaniotto.pedidos_api.dto.ClienteRequest;
import com.maicosmaniotto.pedidos_api.dto.ClienteResponse;
import com.maicosmaniotto.pedidos_api.dto.PageDTO;
import com.maicosmaniotto.pedidos_api.dto.mapper.ClienteMapper;
import com.maicosmaniotto.pedidos_api.exception.RegistroNaoEncontradoException;
import com.maicosmaniotto.pedidos_api.model.Cliente;
import com.maicosmaniotto.pedidos_api.model.Municipio;
import com.maicosmaniotto.pedidos_api.service.ClienteService;

import jakarta.servlet.ServletException;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    private static final String API = "/api/clientes";
    private static final String API_ID = "/api/clientes/{id}";

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    @Spy
    private ClienteMapper clienteMapper = new ClienteMapper();

    @BeforeEach
    void setUp() {
        ProxyFactory factory = new ProxyFactory(clienteController);
        factory.addAdvice(new ValidationAdvice());
        clienteController = (ClienteController) factory.getProxy();
    }

    @Test
    @DisplayName("Deve retornar uma lista de clientes em formato JSON")
    void testeListar() throws Exception {        
        Municipio municipio = ClienteDadosTeste.criarMunicipioValido();
        Cliente cliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Cliente 1", municipio);
        ClienteResponse clienteResponse = clienteMapper.toResponse(cliente);
        List<ClienteResponse> clientes = List.of(clienteResponse);
        PageDTO<ClienteResponse> page = new PageDTO<>(clientes, 1, 1, 1, 0);

        when(clienteService.listar(anyInt(), anyInt())).thenReturn(page);

        var requestBuilder = MockMvcRequestBuilders.get(API);
        MockMvcBuilders.standaloneSetup(clienteController)
            .build()
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content", hasSize(clientes.size())))
            .andExpect(jsonPath("totalElements", is(1)))
            .andExpect(jsonPath("$.content[0]._id", is(clienteResponse.id()), Long.class))
            .andExpect(jsonPath("$.content[0].razaoSocial", is(clienteResponse.razaoSocial())))
            .andExpect(jsonPath("$.content[0].tipoPessoa", is(clienteResponse.tipoPessoa())))
            .andExpect(jsonPath("$.content[0].enderecos", hasSize(clienteResponse.enderecos().size())))
            .andExpect(jsonPath("$.content[0].enderecos[0]._id", is(clienteResponse.enderecos().get(0).id()), Long.class));

    }

    @Test
    @DisplayName("Deve retornar um cliente pelo id em formato JSON")
    void testeBuscarPorId() throws Exception {
        Municipio municipio = ClienteDadosTeste.criarMunicipioValido();
        Cliente cliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Cliente 1", municipio);
        cliente.setId(1L);
        ClienteResponse clienteResponse = clienteMapper.toResponse(cliente);

        when(clienteService.buscarPorId(clienteResponse.id())).thenReturn(clienteResponse);

        var requestBuilder = MockMvcRequestBuilders.get(API_ID, cliente.getId());
        MockMvcBuilders.standaloneSetup(clienteController)
            .build()
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("_id", is(clienteResponse.id()), Long.class))
            .andExpect(jsonPath("razaoSocial", is(clienteResponse.razaoSocial())))
            .andExpect(jsonPath("tipoPessoa", is(clienteResponse.tipoPessoa())))
            .andExpect(jsonPath("enderecos", hasSize(clienteResponse.enderecos().size())))
            .andExpect(jsonPath("enderecos[0]._id", is(clienteResponse.enderecos().get(0).id()), Long.class));
    }

    @Test
    @DisplayName("Deve lançar ConstraintViolationException quando o id for negativo")
    void testeBuscarPorIdNegativo() {              
        var requestBuilder = MockMvcRequestBuilders.get(API_ID, -1L);
        assertThrows(ServletException.class, () -> {
            MockMvcBuilders.standaloneSetup(clienteController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isBadRequest());
        });
    }

    @Test
    @DisplayName("Deve lançar RegistroNaoEncontradoException quando o cliente não for encontrado")
    void testeBuscarPorIdNaoEncontrado() {
        when(clienteService.buscarPorId(anyLong())).thenThrow(RegistroNaoEncontradoException.class);

        var requestBuilder = MockMvcRequestBuilders.get(API_ID, 1L);
        assertThrows(ServletException.class, () -> {
            MockMvcBuilders.standaloneSetup(clienteController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
        });
    }

    @Test
    @DisplayName("Deve criar um novo cliente")
    void testeCriar() throws Exception {
        
        ClienteRequest clienteRequest = ClienteDadosTeste.criarClienteRequestDadosValidosComUmEndereco("Cliente 1");
        Cliente cliente = clienteMapper.toEntity(clienteRequest);
        cliente.setId(1L);
        ClienteResponse clienteResponse = clienteMapper.toResponse(cliente);

        when(clienteService.criar(clienteRequest)).thenReturn(clienteResponse);

        var contentJson = new ObjectMapper().writeValueAsString(clienteRequest);
        var requestBuilder = MockMvcRequestBuilders.post(API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(contentJson);
        MockMvcBuilders.standaloneSetup(clienteController)
            .build()
            .perform(requestBuilder)
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("_id", is(clienteResponse.id()), Long.class))
            .andExpect(jsonPath("razaoSocial", is(clienteResponse.razaoSocial())))
            .andExpect(jsonPath("tipoPessoa", is(clienteResponse.tipoPessoa())))
            .andExpect(jsonPath("enderecos", hasSize(clienteResponse.enderecos().size())))
            .andExpect(jsonPath("enderecos[0]._id", is(clienteResponse.enderecos().get(0).id()), Long.class));
    }

    @Test
    @DisplayName("Deve retornar bad request ao tentar criar um cliente com dados inválidos")
    void testeCriarDadosInvalidos() throws Exception {
        List<ClienteResponse> clientes = new ArrayList<>();
        Cliente cliente;

        Municipio municipio = ClienteDadosTeste.criarMunicipioValido();
        cliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Cliente 1", municipio);
        cliente.setRazaoSocial("");
        clientes.add(clienteMapper.toResponse(cliente));

        cliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Cliente 2", municipio);
        cliente.getEnderecos().clear();
        clientes.add(clienteMapper.toResponse(cliente));
        
        for (ClienteResponse clienteResponse : clientes) {
            var contentJson = new ObjectMapper().writeValueAsString(clienteResponse);
            var requestBuilder = MockMvcRequestBuilders.post(API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson);
            MockMvcBuilders.standaloneSetup(clienteController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("Deve atualizar um cliente")
    void testeAtualizar() throws Exception {
        ClienteRequest clienteRequest = ClienteDadosTeste.criarClienteRequestDadosValidosComUmEndereco("Cliente 1");
        
        Municipio municipio = ClienteDadosTeste.criarMunicipioValido();
        Cliente cliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Cliente 1", municipio);
        cliente.setId(1L);
        ClienteResponse clienteResponse = clienteMapper.toResponse(cliente);

        when(clienteService.atualizar(anyLong(), any())).thenReturn(clienteResponse);

        var contentJson = new ObjectMapper().writeValueAsString(clienteRequest);
        var requestBuilder = MockMvcRequestBuilders.put(API_ID, cliente.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(contentJson);
        MockMvcBuilders.standaloneSetup(clienteController)
            .build()
            .perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("_id", is(clienteResponse.id()), Long.class))
            .andExpect(jsonPath("razaoSocial", is(clienteResponse.razaoSocial())))
            .andExpect(jsonPath("tipoPessoa", is(clienteResponse.tipoPessoa())))
            .andExpect(jsonPath("enderecos", hasSize(clienteResponse.enderecos().size())))
            .andExpect(jsonPath("enderecos[0]._id", is(clienteResponse.enderecos().get(0).id()), Long.class));
    }

    @Test
    @DisplayName("Deve lançar RegistroNaoEncontradoException ao tentar atualizar um cliente que não existe")
    void testeAtualizarNaoEncontrado() throws Exception {
        ClienteRequest clienteRequest = ClienteDadosTeste.criarClienteRequestDadosValidosComUmEndereco("Cliente 1");

        when(clienteService.atualizar(anyLong(), any())).thenThrow(RegistroNaoEncontradoException.class);

        var contentJson = new ObjectMapper().writeValueAsString(clienteRequest);
        var requestBuilder = MockMvcRequestBuilders.put(API_ID, 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(contentJson);
        assertThrows(ServletException.class, () -> {
            MockMvcBuilders.standaloneSetup(clienteController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
        });
    }

    @Test
    @DisplayName("Deve retornar bad request ao tentar atualizar um cliente com dados inválidos")
    void testeAtualizarDadosInvalidos() throws Exception {
        Map<Long, ClienteRequest> clientes = new HashMap<>();
        ClienteRequest clienteRequest;

        // Id válido e dados inválidos
        clienteRequest = ClienteDadosTeste.criarClienteRequestDadosValidosComUmEndereco("");

        clientes.put(1L, clienteRequest);
        clienteRequest = ClienteDadosTeste.criarClienteRequestDadosValidosSemEndereco("Cliente 2");
        clientes.put(2L, clienteRequest);

        for (Map.Entry<Long, ClienteRequest> entry : clientes.entrySet()) {
            var contentJson = new ObjectMapper().writeValueAsString(entry.getValue());
            var requestBuilder = MockMvcRequestBuilders.put(API_ID, entry.getKey())
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson);
            MockMvcBuilders.standaloneSetup(clienteController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isBadRequest());
        }

        clientes.clear();

        // Id inválido e dados válidos
        clienteRequest = ClienteDadosTeste.criarClienteRequestDadosValidosComUmEndereco("Cliente 1");
        clientes.put(0L, clienteRequest);
        clienteRequest = ClienteDadosTeste.criarClienteRequestDadosValidosComUmEndereco("Cliente 2");
        clientes.put(-1L, clienteRequest);

        for (Map.Entry<Long, ClienteRequest> entry : clientes.entrySet()) {            
            var contentJson = new ObjectMapper().writeValueAsString(entry.getValue());
            var requestBuilder = MockMvcRequestBuilders.put(API_ID, entry.getKey())
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson);
            assertThrows(ServletException.class, () -> {
                MockMvcBuilders.standaloneSetup(clienteController)
                    .build()
                    .perform(requestBuilder)
                    .andExpect(status().isBadRequest());
            });
        }
    }

    @Test
    @DisplayName("Deve excluir um cliente")
    void testeExcluir() throws Exception {
        doNothing().when(clienteService).excluir(anyLong());

        var requestBuilder = MockMvcRequestBuilders.delete(API_ID, 1L);
        MockMvcBuilders.standaloneSetup(clienteController)
            .build()
            .perform(requestBuilder)
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve lançar RegistroNaoEncontradoException ao tentar excluir um cliente que não existe")
    void testeExcluirNaoEncontrado() {
        doThrow(RegistroNaoEncontradoException.class).when(clienteService).excluir(anyLong());
        var requestBuilder = MockMvcRequestBuilders.delete(API_ID, 1L);
        assertThrows(ServletException.class, () -> {
            MockMvcBuilders.standaloneSetup(clienteController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isNotFound());
        });
    }

    @Test
    @DisplayName("Deve retornar bad request ao tentar excluir um cliente com id inválido")
    void testeExcluirIdInvalido() {
        var requestBuilder = MockMvcRequestBuilders.delete(API_ID, 0L);
        assertThrows(ServletException.class, () -> {
            MockMvcBuilders.standaloneSetup(clienteController)
                .build()
                .perform(requestBuilder)
                .andExpect(status().isBadRequest());
        });
    }

}
