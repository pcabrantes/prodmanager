package br.com.pcabrantes.prodmanager.repository;

import br.com.pcabrantes.prodmanager.entity.Categoria;
import org.springframework.data.repository.CrudRepository;

public interface CategoriaRepository extends CrudRepository<Categoria, Long> {
}
