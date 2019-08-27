package br.com.pcabrantes.prodmanager.config.security;

import br.com.pcabrantes.prodmanager.entity.Cliente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private Long id;
    private String name;
    private String email;
    private String appPassword;
    private List<GrantedAuthority> appAuthorities = new ArrayList<>();

    public UserPrincipal(Long id, String name, String email, String appPassword, List<GrantedAuthority> appAuthorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.appPassword = appPassword;
        this.appAuthorities = appAuthorities;
    }

    public UserPrincipal(Cliente cliente) {
        this.id = cliente.getIdCliente();
        this.name = cliente.getNome();
        this.email = cliente.getEmail();
        this.appPassword = cliente.getSenha();
        this.appAuthorities.add(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return appAuthorities;
    }

    @Override
    public String getPassword() {
        return appPassword;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
