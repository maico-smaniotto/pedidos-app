package com.maicosmaniotto.pedidos_api.service;
import com.maicosmaniotto.pedidos_api.repository.UsuarioRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.maicosmaniotto.pedidos_api.dto.UsuarioAuthentication;
import com.maicosmaniotto.pedidos_api.dto.UsuarioRegister;
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

    public void authenticate(UsuarioAuthentication userAuthenticationDTO) {
     
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userAuthenticationDTO.username(),
                userAuthenticationDTO.password()
            )
        );
    }

    public void register(UsuarioRegister userRegisterDTO) {
        var user = usuarioRepository.findByUsername(userRegisterDTO.username());
        if (user != null) {
            throw new UsuarioJaExisteException(userRegisterDTO.username());
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userRegisterDTO.password());
        Usuario newUser = new Usuario(userRegisterDTO.username(), encryptedPassword, userRegisterDTO.role());
        usuarioRepository.save(newUser);
    }
}
