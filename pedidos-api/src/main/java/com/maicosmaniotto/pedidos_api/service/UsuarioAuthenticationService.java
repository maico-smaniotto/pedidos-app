package com.maicosmaniotto.pedidos_api.service;
import com.maicosmaniotto.pedidos_api.repository.UsuarioRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.maicosmaniotto.pedidos_api.dto.UsuarioAuthenticationRequest;
import com.maicosmaniotto.pedidos_api.dto.UsuarioRegisterRequest;
import com.maicosmaniotto.pedidos_api.exception.UsuarioJaExisteException;
import com.maicosmaniotto.pedidos_api.model.Usuario;

@Validated
@Service
public class UsuarioAuthenticationService {
    
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    
    public UsuarioAuthenticationService(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
    }

    public void autenticar(UsuarioAuthenticationRequest usuarioAuthenticationRequest) {
     
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                usuarioAuthenticationRequest.username(),
                usuarioAuthenticationRequest.password()
            )
        );
    }

    public void registrar(UsuarioRegisterRequest usuarioRegisterRequest) {
        var usuario = usuarioRepository.findByUsername(usuarioRegisterRequest.username());
        if (usuario != null) {
            throw new UsuarioJaExisteException(usuarioRegisterRequest.username());
        }

        String senhaEncriptada = new BCryptPasswordEncoder().encode(usuarioRegisterRequest.password());
        Usuario novoUsuario = new Usuario(usuarioRegisterRequest.username(), senhaEncriptada, usuarioRegisterRequest.role());
        usuarioRepository.save(novoUsuario);
    }
}
