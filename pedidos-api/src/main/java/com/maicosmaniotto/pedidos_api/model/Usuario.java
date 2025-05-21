package com.maicosmaniotto.pedidos_api.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maicosmaniotto.pedidos_api.enums.StatusRegistro;
import com.maicosmaniotto.pedidos_api.enums.UserRole;
import com.maicosmaniotto.pedidos_api.enums.converters.StatusRegistroConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "usuarios")
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("_id")
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "usuario", length = 100, nullable = false)
    private String username;

    @NotNull
    @NotBlank
    // @Size(min = 8, max = 100)
    @Column(name = "senha", length = 100, nullable = false)
    private String password;

    @NotNull
    private UserRole role;

    @NotNull
    @Convert(converter = StatusRegistroConverter.class)
    @Column(name = "status_registro", length = 1, nullable = false)
    private StatusRegistro statusRegistro = StatusRegistro.ATIVO;

    public Usuario(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        switch (this.role) {
            case ADMIN:
                return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
            case USER:
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
            default:
                return List.of();
        }        
    }

    @Override
    public boolean isAccountNonExpired() {        
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {        
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {        
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.statusRegistro == StatusRegistro.ATIVO;
        // return UserDetails.super.isEnabled();
    }

}
