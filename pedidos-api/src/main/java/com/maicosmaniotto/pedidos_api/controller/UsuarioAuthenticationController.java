package com.maicosmaniotto.pedidos_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maicosmaniotto.pedidos_api.dto.UsuarioAuthentication;
import com.maicosmaniotto.pedidos_api.dto.UsuarioRegister;
import com.maicosmaniotto.pedidos_api.service.UsuarioAuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UsuarioAuthenticationController {

    AuthenticationManager authenticationManager;
    UsuarioAuthenticationService userAuthenticationService;

    public UsuarioAuthenticationController(
        AuthenticationManager authenticationManager,
        UsuarioAuthenticationService userAuthenticationService
    ) {
        this.authenticationManager = authenticationManager;
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UsuarioAuthentication userAuthenticationDTO) {        
        userAuthenticationService.authenticate(userAuthenticationDTO);              
        return ResponseEntity.ok("Login realizado com sucesso");        
    }

    @PostMapping("registrar")
    public ResponseEntity<String> register(@RequestBody @Valid UsuarioRegister registerDTO) {
        try {
            userAuthenticationService.register(registerDTO);
            return ResponseEntity.ok("Usuário registrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
