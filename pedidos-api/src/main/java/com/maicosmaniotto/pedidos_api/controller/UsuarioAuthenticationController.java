package com.maicosmaniotto.pedidos_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maicosmaniotto.pedidos_api.dto.UsuarioAuthenticationRequest;
import com.maicosmaniotto.pedidos_api.dto.UsuarioRegisterRequest;
import com.maicosmaniotto.pedidos_api.service.UsuarioAuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UsuarioAuthenticationController {

    AuthenticationManager authenticationManager;
    UsuarioAuthenticationService usuarioAuthenticationService;

    public UsuarioAuthenticationController(
        AuthenticationManager authenticationManager,
        UsuarioAuthenticationService usuarioAuthenticationService
    ) {
        this.authenticationManager = authenticationManager;
        this.usuarioAuthenticationService = usuarioAuthenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UsuarioAuthenticationRequest usuarioAuthenticationRequest) {        
        usuarioAuthenticationService.autenticar(usuarioAuthenticationRequest);              
        return ResponseEntity.ok("Login realizado com sucesso");        
    }

    @PostMapping("registrar")
    public ResponseEntity<String> registrar(@RequestBody @Valid UsuarioRegisterRequest usuarioRegisterRequest) {
        try {
            usuarioAuthenticationService.registrar(usuarioRegisterRequest);
            return ResponseEntity.ok("Usuário registrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
