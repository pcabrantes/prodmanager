package br.com.pcabrantes.prodmanager.repository;

import br.com.pcabrantes.prodmanager.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClienteRepository extends CrudRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);
}
