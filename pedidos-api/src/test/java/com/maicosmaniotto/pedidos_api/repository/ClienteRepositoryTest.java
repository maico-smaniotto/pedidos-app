package com.maicosmaniotto.pedidos_api.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.maicosmaniotto.pedidos_api.data.ClienteDadosTeste;
import com.maicosmaniotto.pedidos_api.enums.TipoPessoa;
import com.maicosmaniotto.pedidos_api.model.Cliente;
import com.maicosmaniotto.pedidos_api.model.Municipio;

@ActiveProfiles("test")
@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Deve listar clientes com endereços")
    void testFindAll() {

        Municipio municipio = entityManager.persist(ClienteDadosTeste.criarMunicipioValido());

        Cliente cliente;
        cliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Maico Smaniotto", municipio);
        cliente.setTipoPessoa(TipoPessoa.FISICA);
        cliente.setCpfCnpj("12345678901");
        cliente.setNomeFantasia("Maico Smaniotto");
        entityManager.persist(cliente);

        cliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("SmaniSoft Tecnologia", municipio);        
        cliente.setTipoPessoa(TipoPessoa.JURIDICA);
        cliente.setCpfCnpj("12345678901");
        cliente.setNomeFantasia("SmaniSoft");
        entityManager.persist(cliente);

        Page<Cliente> clientePage = clienteRepository.findAll(PageRequest.of(0, 10));
        
        assertThat(clientePage)
            .isNotNull()
            .hasSize(2);
        
        assertThat(clientePage.getContent()).isNotEmpty();

        clientePage.getContent().forEach(c -> {
            assertThat(c.getEnderecos()).isNotEmpty();
        });
    }

    @Test
    @DisplayName("Deve buscar cliente por Id")
    void testFindById() {
        Municipio municipio = entityManager.persist(ClienteDadosTeste.criarMunicipioValido());
        
        Cliente cliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Cliente 1", municipio);
        Cliente clienteSalvo = entityManager.persist(cliente);

        Optional<Cliente> clienteEncontrado = clienteRepository.findById(clienteSalvo.getId());

        assertTrue(clienteEncontrado.isPresent());
        assertThat(clienteEncontrado).contains(clienteSalvo);
    }

    @Test
    @DisplayName("Deve salvar um cliente")
    void testSave() {
        Municipio municipio = entityManager.persist(ClienteDadosTeste.criarMunicipioValido());
        
        Cliente cliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Cliente 1", municipio);
        Cliente clienteSalvo = clienteRepository.save(cliente);

        assertThat(clienteSalvo).isNotNull();
        assertThat(clienteSalvo.getId()).isNotNull();
        assertThat(clienteSalvo.getRazaoSocial()).isEqualTo(cliente.getRazaoSocial());
    }

    @Test
    @DisplayName("Deve atualizar um cliente")
    void testUpdate() {
        Municipio municipio = entityManager.persist(ClienteDadosTeste.criarMunicipioValido());
        
        Cliente cliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Cliente 1", municipio);
        Cliente clienteSalvo = entityManager.persist(cliente);

        clienteSalvo.setRazaoSocial("New Name");
        Cliente clienteAtualizado = clienteRepository.save(clienteSalvo);

        assertThat(clienteAtualizado).isNotNull();
        assertThat(clienteAtualizado.getId()).isEqualTo(clienteSalvo.getId());
        assertThat(clienteAtualizado.getRazaoSocial()).isEqualTo("New Name");
    }

    @Test
    @DisplayName("Deve excluir um cliente")
    void testDelete() {
        Municipio municipio = entityManager.persist(ClienteDadosTeste.criarMunicipioValido());
        
        Cliente cliente = ClienteDadosTeste.criarClienteValidoComUmEndereco("Cliente 1", municipio);
        Cliente clienteSalvo = entityManager.persist(cliente);

        clienteRepository.delete(clienteSalvo);

        Optional<Cliente> clienteEncontrado = clienteRepository.findById(clienteSalvo.getId());

        assertTrue(clienteEncontrado.isEmpty());
    }

}
