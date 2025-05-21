package com.maicosmaniotto.pedidos_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.maicosmaniotto.pedidos_api.enums.TipoPessoa;
import com.maicosmaniotto.pedidos_api.enums.UnidadeFederativa;
import com.maicosmaniotto.pedidos_api.model.Cliente;
import com.maicosmaniotto.pedidos_api.model.ClienteEndereco;
import com.maicosmaniotto.pedidos_api.model.Municipio;
import com.maicosmaniotto.pedidos_api.repository.ClienteRepository;

@SpringBootApplication
public class PedidosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PedidosApiApplication.class, args);
	}

	@Bean
	@Profile("dev")
	CommandLineRunner initDatabase(ClienteRepository repository) {
		return args -> {
			repository.deleteAll();
			
			Cliente cliente = new Cliente();
			cliente.setTipoPessoa(TipoPessoa.FISICA);
			cliente.setRazaoSocial("Cliente Teste");
			cliente.setNomeFantasia("Cliente Teste");
			cliente.setCpfCnpj("12345678901");
			
			ClienteEndereco endereco = new ClienteEndereco();
			endereco.setLogradouro("Rua Teste");
			endereco.setNumero("123");
			endereco.setComplemento("Complemento");
			endereco.setBairro("Bairro");
			endereco.setCodigoPostal("12345678");
			endereco.setMunicipio(new Municipio(
				1L, 
				"São Paulo", 
				"1234567", // Código IBGE fictício
				UnidadeFederativa.SP
			));
			endereco.setCliente(cliente);
			cliente.getEnderecos().add(endereco);
			repository.save(cliente);

			System.out.println("Base de dados inicializada");
		};
	}
}
