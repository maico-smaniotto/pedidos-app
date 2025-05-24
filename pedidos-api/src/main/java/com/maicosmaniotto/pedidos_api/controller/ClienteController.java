package com.maicosmaniotto.pedidos_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.maicosmaniotto.pedidos_api.dto.ClienteRequest;
import com.maicosmaniotto.pedidos_api.dto.ClienteResponse;
import com.maicosmaniotto.pedidos_api.dto.PageDTO;
import com.maicosmaniotto.pedidos_api.service.ClienteService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    private final ClienteService clienteService;

	public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
	}

	@GetMapping
    public PageDTO<ClienteResponse> listar(
        @RequestParam(defaultValue = "0")  @PositiveOrZero    int page, 
        @RequestParam(defaultValue = "10") @Positive @Max(50) int pageSize
    ) {
        return clienteService.listar(page, pageSize);
    }

    @GetMapping("/{id}")
    public ClienteResponse buscarPorId(@PathVariable @NotNull @Positive Long id) {
        return clienteService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ClienteResponse criar(@RequestBody @Valid @NotNull ClienteRequest clienteRequest) {
        return clienteService.criar(clienteRequest);
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(@PathVariable @NotNull @Positive Long id, @RequestBody @Valid @NotNull ClienteRequest clienteRequest) {
        return clienteService.atualizar(id, clienteRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable @NotNull @Positive Long id) {
        clienteService.excluir(id);
    }
    
}
