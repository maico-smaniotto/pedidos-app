package com.maicosmaniotto.pedidos_api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.maicosmaniotto.pedidos_api.repository.UsuarioRepository;

@Service
public class UsuarioAuthorizationService implements UserDetailsService {
    
    UsuarioRepository usuarioRepository;

    public UsuarioAuthorizationService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

}
