package br.com.pcabrantes.prodmanager.config.security;

import br.com.pcabrantes.prodmanager.entity.Cliente;
import br.com.pcabrantes.prodmanager.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));
        UserPrincipal userDetails = new UserPrincipal(cliente);

        return userDetails;
    }
}
